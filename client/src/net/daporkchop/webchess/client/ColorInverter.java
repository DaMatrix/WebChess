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

package net.daporkchop.webchess.client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ColorInverter {
    public static void main(String... args) throws Exception  {
        File root = new File("/run/user/1000/gvfs/sftp:host=home.daporkchop.net,user=daporkchop/home/daporkchop/share/School/WebChess/Photos");
        for (File file : root.listFiles())  {
            if (file.getName().startsWith("inverted_")) {
                continue;
            }
            BufferedImage image = ImageIO.read(file);
            for (int x = image.getWidth() - 1; x >= 0; x--) {
                for (int y = image.getHeight() - 1; y >= 0; y--)    {
                    int col = image.getRGB(x, y);
                    if (((col >> 24) & 0xFF) != 0xFF)   {
                    } else {
                        if ((col & 0xFF) < 128) {
                            image.setRGB(x, y, 0xFFFFFFFF);
                        } else {
                            image.setRGB(x, y, 0xFF000000);
                        }
                    }
                }
            }
            ImageIO.write(image, "png", new File(root, "inverted_" + file.getName()));
        }
    }
}
