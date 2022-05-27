package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TypeFetcher {

	public static <T> List<T> getAllType(Class<T> type, BasicElement<?> element) {
		List<T> list = new ArrayList<>();
		TypeFetcher.addElementType(list, type, element);
		return list;
	}

	public static <T> void addElementType(List<T> list, Class<T> type, BasicElement<?> element) {
		if (element.getClass().isAssignableFrom(type)) {
			list.add(type.cast(element));
		}
		element.children.forEach(elem -> {
			TypeFetcher.addElementType(list, type, elem);
		});
	}

	public static <T> List<Pair<BasicElement<?>, T>> getListenerType(Class<T> type, BasicElement<?> element) {
		List<Pair<BasicElement<?>, T>> list = new ArrayList<>();
		TypeFetcher.addListenerType(list, type, element);
		return list;
	}

	public static <T> void addListenerType(List<Pair<BasicElement<?>, T>> list, Class<T> type, BasicElement<?> element) {

		if (!element.shouldRender()) {
			return;
		}

		if (element.outlineElement != null) {
			element.outlineElement.listeners.forEach(listener -> {
				if (type.isAssignableFrom(listener.getClass())) {
					list.add(new Pair<>(element.outlineElement, type.cast(listener)));
				}
			});
		}

		element.listeners.forEach(listener -> {
			if (type.isAssignableFrom(listener.getClass())) {
				list.add(new Pair<>(element, type.cast(listener)));
			}
		});

		element.children.forEach(elem -> {
			TypeFetcher.addListenerType(list, type, elem);
		});
	}
}