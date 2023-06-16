package com.landsem.setting.upgrade;

import java.util.List;

public class EasyTool {

	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == null)
			return obj2 == null;
		else
			return obj1.equals(obj2);
	}

	public static boolean isAllEquals(Object... objs) {
		if (objs.length % 2 != 0)
			throw new IllegalArgumentException();
		for (int i = 0; i < objs.length - 1; i += 2) {
			if (equals(objs[i], objs[i + 1]) == false)
				return false;
		}
		return true;
	}

	public static boolean isDeeplyEquals(List<?> list1, List<?> list2) {
		if (isAllNull(list1, list2))
			return true;
		if (isAllNotNull(list1, list2) == false)
			return false;
		if (list1.size() != list2.size())
			return false;
		for (int index = 0, size = list1.size(); index < size; index++) {
			Object ele1 = list1.get(index);
			Object ele2 = list2.get(index);
			if (equals(ele1, ele2) == false)
				return false;
		}
		return true;
	}

	public static boolean isAllNull(Object... objs) {
		for (Object obj : objs)
			if (obj != null)
				return false;
		return true;
	}

	public static boolean isAllNotNull(Object... objs) {
		for (Object obj : objs)
			if (obj == null)
				return false;
		return true;
	}

	public static <T> boolean in(T ele, T... elements) {
		for (T t : elements) {
			if (t == ele)
				return true;
		}
		return false;
	}

	public static <T> boolean inArray(T ele, T[] elements) {
		for (T t : elements) {
			if (t == ele)
				return true;
		}
		return false;
	}

	public static int parseBoolArrayToInt(boolean... bs) {
		if (bs.length > Integer.SIZE) {
			throw new IllegalArgumentException();
		}
		int result = 0;
		for (int i = 0; i < bs.length; i++) {
			int bitValue = bs[i] ? 1 : 0;
			result |= (bitValue << i);
		}
		return result;
	}
}
