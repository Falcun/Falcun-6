package falcun.net.api.gui.animations;

import falcun.net.api.gui.GuiUtils;
import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.effects.Effect;
import falcun.net.api.gui.region.GuiRegion;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public final class FalcunSaberSnake extends Effect {
	Component component;
	public Supplier<Integer> color;
	public boolean draw = true;

	public FalcunSaberSnake(Component component, Supplier<Integer> color) {
		this.component = component;
		this.color = color;
		this.snakes = new Snake[]{new Snake(Movement.RIGHT), new Snake(Movement.LEFT)};
	}

	public FalcunSaberSnake(Component component, int color) {
		this(component, () -> color);
	}

	Snake[] snakes;

	private class Snake {
		Location[] l = new Location[FalcunSaberSnake.this.component.region.width >> 2];
		Movement movement;

		Snake(Movement movement) {
			this.movement = movement;
			GuiRegion gr = FalcunSaberSnake.this.component.region.duplicate();
			final int minx = gr.x;
			final int max = gr.getRight();
			final int miny = gr.y;
			final int maxy = gr.getBottom();
			int x = minx;
			int y = miny;
			if (movement == Movement.LEFT) {
				x = max;
				y = maxy;
			} else if (movement == Movement.UP) {
				y = maxy;
			} else if (movement == Movement.DOWN) {
				x = max;
			}

			for (int i = 0; i < l.length; ++i) {
				Location loc = new Location(x, y);
				l[i] = loc;
				switch (this.movement) {
					case RIGHT:
						++x;
						if (x > max) {
							x = max;
							this.movement = Movement.DOWN;
							++y;
						}
						continue;
					case DOWN:
						++y;
						if (y > maxy) {
							y = maxy;
							this.movement = Movement.LEFT;
							--x;
						}
						continue;
					case LEFT:
						--x;
						if (x < minx) {
							x = minx;
							this.movement = Movement.UP;
							--y;
						}
						continue;
					case UP:
						--y;
						if (y < miny) {
							y = miny;
							this.movement = Movement.RIGHT;
							++x;
						}
						continue;
					default:
						break;
				}
			}
		}

		void move() {
			for (int i = l.length - 1; i > -1; --i) {
				Location loc = l[i];
				if (i == 0) {
					GuiRegion gr = component.region;
					int minx = gr.x;
					int max = gr.getRight();
					int miny = gr.y;
					int maxy = gr.getBottom();
					switch (this.movement) {
						case RIGHT:
							++loc.x;
							if (loc.x > max) {
								loc.x = max;
								this.movement = Movement.DOWN;
								++loc.y;
							}
							continue;
						case DOWN:
							++loc.y;
							if (loc.y > maxy) {
								loc.y = maxy;
								this.movement = Movement.LEFT;
								--loc.x;
							}
							continue;
						case LEFT:
							--loc.x;
							if (loc.x < minx) {
								loc.x = minx;
								this.movement = Movement.UP;
								--loc.y;
							}
							continue;
						case UP:
							--loc.y;
							if (loc.y < miny) {
								loc.y = miny;
								this.movement = Movement.RIGHT;
								++loc.x;
							}
							continue;
						default:
							break;
					}
					break;
				}
				l[i] = l[i - 1].copy();
			}
		}
	}

	enum Movement {
		RIGHT, DOWN, LEFT, UP
	}

	private final class Location {
		int x;
		int y;

		Location(int x, int y) {
			this.x = x;
			this.y = y;
		}

		Location copy() {
			return new Location(x, y);
		}
	}


	private long lastMove = 0L;

	@Override
	public void draw(int mX, int mY, Component component, Phase phase) {
		if (!draw)return;
		long now  = System.currentTimeMillis();
		boolean doMove = now - lastMove > 6L;
		if (doMove){
			lastMove = now;
		}
		GuiUtils.setColor(this.color.get());
		GL11.glLineWidth(2.4f);
		for (Snake snake : snakes) {
			if (doMove) snake.move();
			for (Location l : snake.l) {
				GuiUtils.drawNonTextured((tess, wr) -> {
					GL11.glEnable(GL11.GL_LINE_SMOOTH);
					wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
					GuiUtils.drawRoundedSquare(wr, new GuiRegion(l.x, l.y-1, 1, 1), 0);
					tess.draw();
					GL11.glDisable(GL11.GL_LINE_SMOOTH);
				});
			}
		}
		GL11.glLineWidth(1f);
	}


}
