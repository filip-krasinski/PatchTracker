package net.neszku.patch_tracker.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.neszku.patch_tracker.Emote;
import net.neszku.patch_tracker.PatchTracker;
import net.neszku.patch_tracker.page.IPageCluster;

public class ReactionListener extends ListenerAdapter {

    private final PatchTracker instance;

    public ReactionListener(PatchTracker instance) {
        this.instance = instance;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser() == null) {
            return;
        }

        if (event.getUser().isBot()) {
            return;
        }

        TextChannel channel = event.getTextChannel();
        channel.retrieveMessageById(event.getMessageId()).queue(mess -> {
            if (!mess.getAuthor().equals(instance.getShardManager().getShards().get(0).getSelfUser())) {
                return;
            }

            if (mess.getEmbeds().isEmpty()) {
                return;
            }

            MessageEmbed embed = mess.getEmbeds().get(0);
            MessageEmbed.Footer footer = embed.getFooter();
            if (footer == null || footer.getText() == null) {
                return;
            }

            event.getReaction().removeReaction(event.getUser()).queue();

            boolean isRight = Emote.RIGHT.getId().equals(event.getReactionEmote().getId());
            boolean isLeft  = Emote.LEFT.getId().equals(event.getReactionEmote().getId());

            String[] indexs   = footer.getText().substring("Page".length()).replace(" ", "").split("/");
            String identifier = footer.getIconUrl().split("\\?")[1];
            int currPage      = Integer.parseInt(indexs[0]);
            int totalPage     = Integer.parseInt(indexs[1]);

            if (currPage == totalPage && isRight) {
                return;
            }

            if (currPage == 1 && isLeft) {
                return;
            }

            IPageCluster<String> cluster = instance.getPatchService().getCache().get(identifier);
            if (cluster == null) {
                return;
            }

            cluster.setCurrentPage(currPage - 1);

            if (isRight && cluster.hasNextPage()){
                cluster.nextPage();
            }
            else if (isLeft && cluster.hasPreviousPage()) {
                cluster.previousPage();
            }

            mess.editMessage(edit(embed, cluster)).queue();
        });
    }

    public static MessageEmbed edit(MessageEmbed embed, IPageCluster<String> cluster) {
        return new EmbedBuilder()
                .setColor(embed.getColor())
                .setDescription(cluster.getCurrentPage().getData())
                .setThumbnail(embed.getThumbnail().getUrl())
                .setImage(embed.getImage() == null ? null : embed.getImage().getUrl())
                .setTitle(embed.getTitle(), embed.getUrl())
                .setFooter(String.format("Page %s / %s",
                        cluster.getCurrentIndex() + 1,
                        cluster.size()),
                        embed.getFooter().getIconUrl()
                ).setTimestamp(embed.getTimestamp())
                .build();
    }
}
