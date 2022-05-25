package falcun.net.api.fps.config;

import falcun.net.api.fps.FalcunFPS;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunSetting;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class FalcunFPSField<T> {

	private final Field field;
	private final FalcunFPS module;
	private FalcunBounds bounds;
	private final FalcunSetting value;
	private Class<?> parameterizedFieldType = Object.class;
	private final Class<?> fieldType;

	public FalcunFPSField(Field field, FalcunFPS module, FalcunSetting configProperty) {
		this.field = field;
		this.fieldType = field.getType();
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] typeArgs = parameterizedType.getActualTypeArguments();
			if (typeArgs != null && typeArgs.length > 0) {
				this.parameterizedFieldType = (Class<?>) typeArgs[0];
			}
		}
		this.module = module;
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		if (field.isAnnotationPresent(FalcunBounds.class)) {
			this.bounds = field.getAnnotation(FalcunBounds.class);
		}
		this.value = configProperty;
	}


	public T getValue() {
		try {
			return (T) this.field.get(module);
		} catch (Throwable err) {
			err.printStackTrace();
		}
		return null;
	}

	public void setValue(T value) {
		try {
			this.field.set(this.module, value);
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	public FalcunBounds getBounds() {
		return this.bounds;
	}

	public FalcunSetting getSetting() {
		return this.value;
	}

	public Class<?> getParameterizedFieldType() {
		return this.parameterizedFieldType;
	}

	public Class<?> getFieldType() {
		return this.fieldType;
	}
}
