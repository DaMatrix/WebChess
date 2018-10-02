package net.daporkchop.webchess.common.util;

import lombok.NonNull;

/**
 * @author DaPorkchop_
 */
public interface Constants {
    boolean IDE = "true".equalsIgnoreCase(System.getProperty("webchess.ide", "false"));

    int NETWORK_PORT = 38573;

    char[] VALID_USERNAME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_öéäàüè".toCharArray();

    default boolean ensureUsernameValid(@NonNull String username)   {
        if (username.length() < 4)  {
            return false;
        }
        for (int i = username.length() - 1; i >= 0; i--)    {
            char c = username.charAt(i);
            boolean contains = false;
            for (int j = VALID_USERNAME_CHARS.length - 1; j >= 0; j--)  {
                if (VALID_USERNAME_CHARS[j] == c)   {
                    contains = true;
                }
            }
            if (!contains)  {
                return false;
            }
        }
        return true;
    }
}
