package de.westnordost.osmagent.quests.osm.changes;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringMapChangesBuilder
{
	private final Map<String,String> source;
	private final Map<String, StringMapEntryChange> changes;

	public StringMapChangesBuilder(@NonNull Map<String,String> source)
	{
		this.source = source;
		changes = new HashMap<>();
	}

	public void delete(@NonNull String key)
	{
		String valueBefore = source.get(key);
		if(valueBefore == null)
		{
			throw new IllegalArgumentException("The key '" + key + "' does not exist in the map.");
		}
		checkDuplicate(key);
		changes.put(key, new StringMapEntryDelete(key, valueBefore));
	}

	public void add(@NonNull String key, @NonNull String value)
	{
		if(source.containsKey(key))
		{
			throw new IllegalArgumentException("The key '" + key + "' already exists in the map.");
		}
		checkDuplicate(key);
		changes.put(key, new StringMapEntryAdd(key, value));
	}

	public void modify(@NonNull String key, @NonNull String value)
	{
		String valueBefore = source.get(key);
		if(valueBefore == null)
		{
			throw new IllegalArgumentException("The key '" + key + "' does not exist in the map.");
		}
		checkDuplicate(key);
		changes.put(key, new StringMapEntryModify(key, valueBefore, value));
	}

	private void checkDuplicate(String key)
	{
		if(changes.containsKey(key))
		{
			throw new IllegalStateException("The key '" + key + "' is already being modified.");
		}
	}

	public StringMapChanges create()
	{
		List<StringMapEntryChange> list = new ArrayList<>();
		list.addAll(changes.values());
		return new StringMapChanges(list);
	}
}
