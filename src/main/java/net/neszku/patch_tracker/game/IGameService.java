package net.neszku.patch_tracker.game;

import javax.annotation.Nullable;
import java.util.Set;

public interface IGameService {

    /**
     * Returns the set of registered games
     *
     * @return set of registered games
     */
    Set<Game> getGames();

    /**
     * Gets the {@link Game} by name
     *
     * @param name name of the game
     * @return {@link Game} if found otherwise null
     */
    @Nullable Game getGame(String name);

    /**
     * Registers the game
     *
     * @param game game to be registered
     */
    void registerGame(Game game);
}
