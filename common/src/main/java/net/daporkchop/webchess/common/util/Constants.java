/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2018 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it. Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.webchess.common.util;

import lombok.NonNull;

/**
 * @author DaPorkchop_
 */
public interface Constants {
    boolean IDE = "true".equalsIgnoreCase(System.getProperty("webchess.ide", "false"));

    int NETWORK_PORT = 38573;

    int USERNAME_LENGTH_MIN = 5;
    int USERNAME_LENGTH_MAX = 16;
    int PASSWORD_LENGTH_MIN = 4;

    char[] VALID_USERNAME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_öéäàüè".toCharArray();

    default boolean ensureUsernameValid(@NonNull String username) {
        if (username.length() < USERNAME_LENGTH_MIN) {
            return false;
        } else if (username.length() > USERNAME_LENGTH_MAX) {
            return false;
        }
        for (int i = username.length() - 1; i >= 0; i--) {
            char c = username.charAt(i);
            boolean contains = false;
            for (int j = VALID_USERNAME_CHARS.length - 1; j >= 0; j--) {
                if (VALID_USERNAME_CHARS[j] == c) {
                    contains = true;
                }
            }
            if (!contains) {
                return false;
            }
        }
        return true;
    }
}
