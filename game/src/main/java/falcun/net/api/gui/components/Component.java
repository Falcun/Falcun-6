package falcun.net.api.gui.components;


import falcun.net.api.gui.effects.Effect;
import falcun.net.api.gui.effects.Effect.Phase;
import falcun.net.api.gui.effects.PinEffect;
import falcun.net.api.gui.region.GuiRegion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public abstract class Component {

    public GuiRegion region;

    public List<Effect> effects = new ArrayList<>();
    public List<Component> subComponents = new LinkedList<>();
    // predicate components from receiving mouse information
    public Predicate<Component> mousePred;
    public boolean ispartofscroll = false;

    public Component(GuiRegion region) {
        this.region = region;
    }

    public void preDraw(int mX, int mY) {
        effects.forEach(it -> it.draw(mX, mY, this, Effect.Phase.BEFORE));
        draw(mX, mY);
    }

    public void afterDraw(int mX, int mY) {
        effects.forEach(it -> it.draw(mX, mY, this, Effect.Phase.AFTER));
    }

    public abstract void draw(int mX, int mY);

    public void draw(int mx, int mY, float p){

    }

    public void onClicked(int mX, int mY, int mouseButton) {

    }

    public void onScroll(int mX, int mY, Direction direction) {

    }

    public enum Direction {
        UP, DOWN
    }

    public void onMouseClick(int mX, int mY, int mouseButton) {
        if(ispartofscroll) return;
        effects.forEach(it -> it.anyClick(mX, mY, mouseButton, this, Effect.Phase.BEFORE));
        onClicked(mX, mY, mouseButton);
        effects.forEach(it -> it.anyClick(mX, mY, mouseButton, this, Phase.AFTER));
    }

    public boolean isOver(int mX, int mY) {
        return isOver(region, mX, mY);
    }

    public void onKey(int keyCode, char letter) {

    }

    public static boolean isOver(GuiRegion area, int mX, int mY) {
        return mX > area.x && mX < area.getRight() && mY > area.y && mY < area.getBottom();
    }


    public PinEffect pinTo(Component component) {
        PinEffect pin = new PinEffect(component, this);
        effects.add(pin);
        return pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(region, component.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region);
    }

    // returns first effect to type
    @SuppressWarnings("unchecked")
    public <T> T getEffectType(Class<T> klass) {
        for (Effect effect : effects) {
            if (effect.getClass().equals(klass)) {
                return (T) effect;
            }
        }
        return null;
    }
}
