package net.neszku.patch_tracker.game.impl;

import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.game.IGameService;
import org.pmw.tinylog.Logger;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class GameServiceImpl implements IGameService {

    private final Set<Game> games = new HashSet<>();

    @Override
    public Set<Game> getGames() {
        return new HashSet<>(games);
    }

    @Override
    public @Nullable Game getGame(String name) {
        return games.stream()
                .filter(game ->
                    game.getFullName().equalsIgnoreCase(name)
                )
                .findAny()
                .orElse(null);
    }

    @Override
    public void registerGame(Game game) {
        Logger.info("\tRegistering game '{}'", game.getFullName());
        games.add(game);
    }
}
