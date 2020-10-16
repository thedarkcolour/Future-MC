package thedarkcolour.futuremc.feature;

import com.google.common.collect.AbstractIterator;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class BlockPosUtil {
    public static Iterable<BlockPos> getColumnHeightmap(Random rand, int blockLimit, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        int xRange = maxX - minX + 1;
        int yRange = maxY - minY + 1;
        int zRange = maxZ - minZ + 1;

        return () -> new AbstractIterator<BlockPos>() {
            private final BlockPos.Mutable pos = new BlockPos.Mutable();
            private int remaining = blockLimit;

            protected BlockPos computeNext() {
                if (remaining <= 0) {
                    return endOfData();
                } else {
                    BlockPos blockpos = pos.setPos(minX + rand.nextInt(xRange), minY + rand.nextInt(yRange), minZ + rand.nextInt(zRange));
                    --remaining;
                    return blockpos;
                }
            }
        };
    }

    public static Iterable<BlockPos> iterateOutwards(BlockPos origin, int xRange, int yRange, int zRange) {
        int i = xRange + yRange + zRange;
        int j = origin.getX();
        int k = origin.getY();
        int l = origin.getZ();
        return () -> new AbstractIterator<BlockPos>() {
            private final BlockPos.Mutable cursor = new BlockPos.Mutable();
            private int currentDepth;
            private int maxX;
            private int maxY;
            private int x;
            private int y;
            private boolean zMirror;

            protected BlockPos computeNext() {
                if (this.zMirror) {
                    this.zMirror = false;
                    this.cursor.setZ(l - (this.cursor.getZ() - l));
                    return this.cursor;
                } else {
                    BlockPos blockPos;
                    for(blockPos = null; blockPos == null; ++this.y) {
                        if (this.y > this.maxY) {
                            ++this.x;
                            if (this.x > this.maxX) {
                                ++this.currentDepth;
                                if (this.currentDepth > i) {
                                    return this.endOfData();
                                }

                                this.maxX = Math.min(xRange, this.currentDepth);
                                this.x = -this.maxX;
                            }

                            this.maxY = Math.min(yRange, this.currentDepth - Math.abs(this.x));
                            this.y = -this.maxY;
                        }

                        int i1 = this.x;
                        int j1 = this.y;
                        int k1 = this.currentDepth - Math.abs(i1) - Math.abs(j1);
                        if (k1 <= zRange) {
                            this.zMirror = k1 != 0;
                            blockPos = this.cursor.setPos(j + i1, k + j1, l + k1);
                        }
                    }

                    return blockPos;
                }
            }
        };
    }
}
