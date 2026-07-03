package org.yuemi.mmomechanics.plugin.skill.mechanic.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.time.Duration;
import java.util.Collection;

/**
 * Displays title and subtitle text overlays on player screens.
 */
public final class SendTitleMechanic implements Mechanic {
    private final String titleText;
    private final String subtitleText;
    private final int fadeInTicks;
    private final int stayTicks;
    private final int fadeOutTicks;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public SendTitleMechanic(
            @NotNull String titleText,
            @NotNull String subtitleText,
            int fadeInTicks,
            int stayTicks,
            int fadeOutTicks
    ) {
        this.titleText = titleText;
        this.subtitleText = subtitleText;
        this.fadeInTicks = fadeInTicks;
        this.stayTicks = stayTicks;
        this.fadeOutTicks = fadeOutTicks;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        
        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeInTicks * 50L),
                Duration.ofMillis(stayTicks * 50L),
                Duration.ofMillis(fadeOutTicks * 50L)
        );

        for (Target target : targets) {
            if (target.getAsEntity() instanceof Player player) {
                String parsedTitle = titleText;
                String parsedSubtitle = subtitleText;
                
                if (api != null) {
                    parsedTitle = api.parsePlaceholders(target, titleText);
                    parsedSubtitle = api.parsePlaceholders(target, subtitleText);
                }

                Component titleComp = miniMessage.deserialize(parsedTitle);
                Component subComp = miniMessage.deserialize(parsedSubtitle);

                Title title = Title.title(titleComp, subComp, times);
                player.showTitle(title);
            }
        }
    }
}

