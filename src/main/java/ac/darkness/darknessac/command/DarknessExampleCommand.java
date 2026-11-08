package ac.darkness.darknessac.command;

import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("deprecation")
public final class DarknessExampleCommand implements CommandExecutor, TabCompleter {

    public static final String PREFIX = "§4§lDarkness §8|";

    private static final List<String> SUBCOMMANDS = Arrays.asList("bypass", "info", "flag", "punish", "alert", "help");
    private static final List<String> BYPASS_TIMES = Arrays.asList("10", "30", "60", "300");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("darknessexample.admin")) {
            sender.sendMessage(color(PREFIX + " &cYou don't have permission!"));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(" ");
            sender.sendMessage(color(PREFIX + " &7Available commands:"));
            sender.sendMessage(color(" &8» &c/" + label + " bypass <player> [sec] [check/scope] &f- &7Grant temporary bypass"));
            sender.sendMessage(color(" &8» &c/" + label + " info <player> &f- &7Inspect player profile"));
            sender.sendMessage(color(" &8» &c/" + label + " flag <player> <check> [type] [verbose] &f- &7Simulate check violation"));
            sender.sendMessage(color(" &8» &c/" + label + " punish <player> [reason] &f- &7Execute punishment"));
            sender.sendMessage(color(" &8» &c/" + label + " alert <message> &f- &7Broadcast staff alert"));
            sender.sendMessage(" ");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "bypass" -> {
                if (args.length < 2) {
                    sender.sendMessage(color(PREFIX + " &cCorrect syntax: /" + label + " bypass <player> [sec] [scope]"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(color(PREFIX + " &cPlayer not found!"));
                    return true;
                }
                DarknessUser user = DarknessAPI.getUser(target);
                if (user == null) {
                    sender.sendMessage(color(PREFIX + " &cDarkness user not found!"));
                    return true;
                }

                long seconds = 10;
                if (args.length >= 3) {
                    try {
                        seconds = Long.parseLong(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(color(PREFIX + " &cInvalid seconds: " + args[2]));
                        return true;
                    }
                }

                if (args.length >= 4) {
                    String scope = args[3].toLowerCase();
                    if (scope.equals("combat") || scope.equals("movement") || scope.equals("world")) {
                        user.setCategoryTempBypass(scope, seconds);
                        sender.sendMessage(color(PREFIX + " &fGranted &e" + scope + " &fbypass to &c" + target.getName() + " &ffor &c" + seconds + "s"));
                    } else {
                        user.setTempBypass(args[3], seconds);
                        sender.sendMessage(color(PREFIX + " &fGranted &e" + args[3] + " &fcheck bypass to &c" + target.getName() + " &ffor &c" + seconds + "s"));
                    }
                    return true;
                }

                user.setTempBypass(seconds);
                sender.sendMessage(color(PREFIX + " &fGranted temporary bypass to &c" + target.getName() + " &ffor &c" + seconds + "s"));
            }
            case "info" -> {
                if (args.length < 2) {
                    sender.sendMessage(color(PREFIX + " &cCorrect syntax: /" + label + " info <player>"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(color(PREFIX + " &cPlayer not found!"));
                    return true;
                }
                DarknessUser user = DarknessAPI.getUser(target);
                if (user == null) {
                    sender.sendMessage(color(PREFIX + " &cDarkness user not found!"));
                    return true;
                }

                sender.sendMessage(" ");
                sender.sendMessage(color(PREFIX + " &7Player profile: &f" + target.getName()));
                sender.sendMessage(" ");
                sender.sendMessage(color(" &8» &7Ping: &f" + user.getTransactionPing() + "ms"));
                sender.sendMessage(color(" &8» &7Version: &f" + user.getVersionName()));
                sender.sendMessage(color(" &8» &7Client: &f" + (user.getBrand() != null ? user.getBrand() : "Vanilla")));
                sender.sendMessage(color(" &8» &7Trust: " + user.getTrustLevel().name() + " &8(&7" + user.getTrustScore() + "%&8)"));
                sender.sendMessage(color(" &8» &7Bypassing: " + (user.isTempBypassing() ? "&eTemporary" : (user.isBypassing() ? "&aYes" : "&cNo"))));
                sender.sendMessage(color(" &8» &7Violations: &c" + (int) user.getTotalViolations() + " &8(&7C: &c" + (int) user.getCombatViolations() + " &7M: &e" + (int) user.getMovementViolations() + " &7W: &b" + (int) user.getWorldViolations() + "&8)"));

                Collection<CheckData> checks = user.getCheckData();
                boolean flagged = false;
                for (CheckData check : checks) {
                    if (check.violations() > 0) {
                        if (!flagged) {
                            sender.sendMessage(" ");
                            sender.sendMessage(color(" &8» &7Active flags:"));
                            flagged = true;
                        }
                        sender.sendMessage(color("    &8- &7" + check.fullCheckName() + " &8(&c" + (int) check.violations() + " VL&8)"));
                    }
                }
                if (!flagged) {
                    sender.sendMessage(color(" &8» &7Active flags: &aClean"));
                }
                sender.sendMessage(" ");
            }
            case "flag" -> {
                if (args.length < 3) {
                    sender.sendMessage(color(PREFIX + " &cCorrect syntax: /" + label + " flag <player> <check> [type] [verbose]"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(color(PREFIX + " &cPlayer not found!"));
                    return true;
                }
                DarknessUser user = DarknessAPI.getUser(target);
                if (user == null) {
                    sender.sendMessage(color(PREFIX + " &cDarkness user not found!"));
                    return true;
                }

                String check = args[2];
                String type = "";
                String verbose = "Manual test flag";

                if (args.length >= 4) {
                    String candidate = args[3].trim();
                    if (candidate.length() <= 3) {
                        type = candidate;
                        if (args.length >= 5) {
                            verbose = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
                        }
                    } else {
                        verbose = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                    }
                }

                if (user.flag(check, type, verbose)) {
                    sender.sendMessage(color(PREFIX + " &fSimulated flag &c" + check + (type.isEmpty() ? "" : " " + type) + " &ffor &7" + target.getName()));
                } else {
                    sender.sendMessage(color(PREFIX + " &cCheck &7'" + check + "' &cnot found!"));
                }
            }
            case "punish" -> {
                if (args.length < 2) {
                    sender.sendMessage(color(PREFIX + " &cCorrect syntax: /" + label + " punish <player> [reason]"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(color(PREFIX + " &cPlayer not found!"));
                    return true;
                }

                String reason = args.length >= 3 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "Cheating";
                DarknessUser user = DarknessAPI.getUser(target);
                if (user != null) {
                    user.punish(reason);
                } else {
                    DarknessAPI.get().punish(target, reason);
                }
                sender.sendMessage(color(PREFIX + " &fPunished &c" + target.getName() + " &8(&7Reason: &f" + reason + "&8)"));
            }
            case "alert" -> {
                if (args.length < 2) {
                    sender.sendMessage(color(PREFIX + " &cCorrect syntax: /" + label + " alert <message>"));
                    return true;
                }
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                DarknessAPI.get().getAlertManager().sendAlert(message);
                sender.sendMessage(color(PREFIX + " &fAlert sent to staff"));
            }
            default -> sender.sendMessage(color(PREFIX + " &cUnknown subcommand! Use &f/" + label + " help"));
        }
        return true;
    }

    @Override
    public @NonNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, completions);
            return completions;
        }

        String sub = args[0].toLowerCase();
        if (args.length == 2 && !sub.equals("alert") && !sub.equals("help")) {
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                names.add(p.getName());
            }
            StringUtil.copyPartialMatches(args[1], names, completions);
            return completions;
        }

        if (sub.equals("bypass")) {
            if (args.length == 3) {
                StringUtil.copyPartialMatches(args[2], BYPASS_TIMES, completions);
                return completions;
            }
            if (args.length == 4) {
                List<String> scopes = new ArrayList<>(Arrays.asList("combat", "movement", "world"));
                DarknessUser user = resolveUser(args[1]);
                if (user != null) {
                    for (CheckData cd : user.getCheckData()) {
                        if (!scopes.contains(cd.getCheck())) {
                            scopes.add(cd.getCheck());
                        }
                    }
                }
                StringUtil.copyPartialMatches(args[3], scopes, completions);
                return completions;
            }
        }

        if (sub.equals("flag")) {
            DarknessUser user = resolveUser(args[1]);
            if (user == null) return completions;

            if (args.length == 3) {
                List<String> checks = new ArrayList<>();
                for (CheckData cd : user.getCheckData()) {
                    if (!checks.contains(cd.getCheck())) {
                        checks.add(cd.getCheck());
                    }
                }
                StringUtil.copyPartialMatches(args[2], checks, completions);
                return completions;
            }

            if (args.length == 4) {
                List<String> types = new ArrayList<>();
                for (CheckData cd : user.getCheckData()) {
                    if (cd.getCheck().equalsIgnoreCase(args[2])) {
                        types.add(cd.getType());
                    }
                }
                StringUtil.copyPartialMatches(args[3], types, completions);
                return completions;
            }
        }

        return completions;
    }

    private static DarknessUser resolveUser(String name) {
        Player p = Bukkit.getPlayer(name);
        if (p != null) {
            DarknessUser u = DarknessAPI.getUser(p);
            if (u != null) return u;
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            DarknessUser u = DarknessAPI.getUser(online);
            if (u != null) return u;
        }
        return null;
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
