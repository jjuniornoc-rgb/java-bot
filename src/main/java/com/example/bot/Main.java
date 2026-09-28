package com.example.bot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.Color;
import java.time.Instant;

public class Main extends ListenerAdapter {

    private static final long startTime = System.currentTimeMillis();
    private static String guildId;

    public static void main(String[] args) {
        // Carrega as variáveis do .env
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String token = dotenv.get("DISCORD_TOKEN");
        guildId = dotenv.get("GUILD_ID");

        if (token == null || token.isBlank()) {
            System.err.println("❌ ERRO: DISCORD_TOKEN não foi informado no arquivo .env");
            System.exit(1);
        }

        try {
            JDABuilder.createDefault(token)
                    .setActivity(Activity.playing("/ping para testar"))
                    .addEventListeners(new Main())
                    .build();
        } catch (Exception e) {
            System.err.println("❌ Falha ao inicializar o cliente JDA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        JDA jda = event.getJDA();
        System.out.println("==================================================");
        System.out.println("🤖 Bot Conectado: " + jda.getSelfUser().getAsTag() + " (ID: " + jda.getSelfUser().getId() + ")");
        System.out.println("⚡ Framework:     JDA 5.x (Java)");
        System.out.println("📡 Servidores:    " + event.getGuildTotalCount());
        System.out.println("==================================================");

        var pingCommand = Commands.slash("ping", "Responde com Pong e métricas de latência do bot em Java!");

        if (guildId != null && !guildId.isBlank()) {
            var guild = jda.getGuildById(guildId);
            if (guild != null) {
                guild.updateCommands().addCommands(pingCommand).queue(
                        success -> System.out.println("✅ Slash Commands sincronizados no servidor [" + guildId + "]!"),
                        error -> System.err.println("⚠️ Erro ao registrar comando na guilda: " + error.getMessage())
                );
            } else {
                System.out.println("⚠️ Servidor não encontrado em cache. Registrando comando globalmente...");
                jda.updateCommands().addCommands(pingCommand).queue();
            }
        } else {
            jda.updateCommands().addCommands(pingCommand).queue(
                    success -> System.out.println("✅ Slash Commands sincronizados globalmente!"),
                    error -> System.err.println("⚠️ Erro ao registrar comandos globais: " + error.getMessage())
            );
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("ping")) {
            return;
        }

        long requestTime = System.currentTimeMillis();

        event.deferReply().queue(hook -> {
            long roundtrip = System.currentTimeMillis() - requestTime;
            long gatewayPing = event.getJDA().getGatewayPing();

            long uptimeMillis = System.currentTimeMillis() - startTime;
            long seconds = (uptimeMillis / 1000) % 60;
            long minutes = (uptimeMillis / (1000 * 60)) % 60;
            long hours = (uptimeMillis / (1000 * 60 * 60));
            String uptimeStr = String.format("%dh %dm %ds", hours, minutes, seconds);

            long totalMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
            long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
            long usedMem = totalMem - freeMem;

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("🏓 Pong! Estatísticas do Bot (Java)")
                    .setDescription("Bot em execução com alta robustez e tipagem estática em **Java (JDA)**!")
                    .setColor(new Color(231, 111, 0)) // Java Orange
                    .setTimestamp(Instant.now())
                    .addField("⚡ Latência de Resposta", "`" + roundtrip + "ms`", true)
                    .addField("📡 Ping WebSocket Discord", "`" + gatewayPing + "ms`", true)
                    .addField("⏱️ Tempo Ativo (Uptime)", "`" + uptimeStr + "`", true)
                    .addField("☕ Framework", "`JDA 5.x`", true)
                    .addField("🟢 Status", "`Online & Operacional`", true)
                    .addField("⚙️ Versão Java", "`v" + System.getProperty("java.version") + "`", true)
                    .addField("🧠 Memória JVM", "`" + usedMem + "MB / " + totalMem + "MB`", true)
                    .setFooter("Solicitado por " + event.getUser().getName(), event.getUser().getEffectiveAvatarUrl());

            hook.editOriginalEmbeds(embed.build()).queue();
        });
    }
}
