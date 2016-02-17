package jeresources.profiling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ProfiledDimensionData
{
	public final ConcurrentMap<String, Integer[]> distributionMap = new ConcurrentHashMap<>();
	public final ConcurrentMap<String, Boolean> silkTouchMap = new ConcurrentHashMap<>();
	public final ConcurrentMap<String, Map<String, Map<Integer, Float>>> dropsMap = new ConcurrentHashMap<>();
}
