package words;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Extend properties to have some useful type-casting methods.
 * 
 * @author abyde
 */
public class Properties extends java.util.Properties {
	private static final long serialVersionUID = 1L;
	private SortedSet<String> sortedKeys = new TreeSet<String>();
	public static final String PARAM_DEBUG = "debug";

	public Properties() {
		super();
	}

	public Properties(String[] args) {
		this();
		this.parseParams(args);
	}

	public Properties(java.util.Properties props) {
		this();
		setAll(props);
	}

	/**
	 * Simple copy of all properties.
	 */
	public Properties copy() {
		Properties p = new Properties();
		for (String key : sortedKeys) {
			String value = getProperty(key);
			p.setProperty(key, value);
		}
		return p;
	}

	public Map<String, String> asMap() {
		Map<String, String> m = new HashMap<String, String>();
		for (Map.Entry<Object, Object> e : entrySet()) {
			String param = e.getKey() + "";
			String value = e.getValue() + "";
			m.put(param, value);
		}
		return m;
	}

	/**
	 * Over-ride so as to add to sortedKeys. If the value is null, the key will
	 * be REMOVED.
	 * 
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	public Object setProperty(String key, String value) {
		if (key == null)
			throw new IllegalArgumentException("null property key");
		if (value == null) {
			sortedKeys.remove(key);
			return super.remove(key);
		} else {
			sortedKeys.add(key);
			return super.setProperty(key, value);
		}
	}

	public void setProps(Properties props) {
		putAll(props);
		for(String key : props.sortedKeys) {
			sortedKeys.add(key);
		}
	}
	
	public String getProperty(String p, String value) {
		String x = super.getProperty(p);
		if (x == null) {
			setProperty(p, value);
			return value;
		}
		return x;
	}

	/**
	 * Construct from a map in which both keys and values are converted to
	 * strings.
	 */
	@SuppressWarnings("rawtypes")
	public static Properties fromMap(Map map) {
		Properties props = new Properties();
		if (map == null)
			return props;
		
		Set entries = map.entrySet();
		for (Object o : entries) {
			Map.Entry entry = (Map.Entry) o;
			props.setProperty("" + entry.getKey(), "" + entry.getValue());
		}
		return props;
	}

	/**
	 * Throw an exception if the named key is not present -- existing method
	 * 'getProperty' just returns null for missing keys.
	 */
	public String getPropertyExceptions(String p) {
		if (this.containsKey(p))
			return super.getProperty(p);
		throw new IllegalArgumentException("Parameter '" + p + "' not set.");
	}

	public long getLong(String p) {
		return Long.parseLong(getPropertyExceptions(p));
	}

	/** Throws an illegal argument exception if p is not set. */
	public int getInt(String p) {
		return Integer.parseInt(getPropertyExceptions(p));
	}

	/** Throws an illegal argument exception if p is not set. */
	public double getDouble(String p) {
		return Double.parseDouble(getPropertyExceptions(p));
	}

	/** Throws an illegal argument exception if p is not set. */
	public boolean getBoolean(String p) {
		return Boolean.parseBoolean(getPropertyExceptions(p));
	}

	public void setInt(String p, int x) {
		setProperty(p, "" + x);
	}

	public void setLong(String p, long x) {
		setProperty(p, "" + x);
	}

	public void setDouble(String p, double x) {
		setProperty(p, "" + x);
	}

	public void setBoolean(String p, boolean x) {
		setProperty(p, "" + x);
	}

	// defaults

	public int getInt(String p, int x) {
		return Integer.parseInt(getProperty(p, "" + x));
	}

	public double getDouble(String p, double x) {
		return Double.parseDouble(getProperty(p, "" + x));
	}

	public boolean getBoolean(String p, boolean x) {
		return Boolean.parseBoolean(getProperty(p, "" + x));
	}

	public long getLong(String p, long x) {
		return Long.parseLong(getProperty(p, "" + x));
	}

	/**
	 * Get the value associated to a parameter, and load it as a Properties
	 * object. If the key is null, return null.
	 * 
	 * The value should have the form: key1:value1, key2:value2, etc.
	 */
	public Properties getProperties(String p, Properties def) {
		if (p == null)
			return null;
		Properties props = new Properties();

		// load from string
		String val = getProperty(p);
		if (val != null)
			props.load(val);
		else
			props.putAll(def);

		return props;
	}

	/**
	 * Load the values in the string val into this object.
	 * 
	 * The string should have the form: key1:value1, key2:value2, etc.
	 */
	public void load(String val) {
		if (val == null)
			return;

		// strip whitespace
		val = val.replaceAll("\\s", "");

		// split on ,
		String[] pairs = val.split(",");
		for (String pair : pairs) {
			// key:value
			String[] kv = pair.split(":", 2);
			setProperty(kv[0], kv[1]);
		}
	}

	public void setProperties(String p, Properties r) {
		setProperty(p, r.toString());
	}

	@SuppressWarnings("rawtypes")
	public void setAll(java.util.Properties props) {
		for (Map.Entry entry : props.entrySet()) {
			setProperty(entry.getKey().toString(), entry.getValue().toString());
		}
	}

	/**
	 * For each string in the array given, parse as 'param=value' and call
	 * p.setProperty(param, value) on each.
	 */
	public void parseParams(String[] args) {
		if (args == null || args.length == 0)
			return;

		for (String s : args) {
			// split on "="
			int splitIndex = s.indexOf('=');
			if (splitIndex < 0)
				throw new IllegalArgumentException(
						"Parameter '"
								+ s
								+ "' is ill-formed.  It should be of the form 'param=value'");
			String param = s.substring(0, splitIndex);
			String value = s.substring(splitIndex + 1, s.length());
			// strip off quotes around the outside
			if (value.length() > 2 && value.startsWith("\"") && value.endsWith("\""))
				value = value.substring(1, value.length()-1);
			setProperty(param, value);
		}
	}

	public void remove(String prop) {
		super.remove(prop);
		sortedKeys.remove(prop);
	}
	
	/**
	 * Report this property set as an array of 'param=value' strings.
	 */
	public String[] getArgs() {
		String[] args = new String[sortedKeys.size()];
		int i = 0;
		for (String param : sortedKeys) {
			args[i++] = param + "=" + getProperty(param);
		}
		return args;
	}

	/**
	 * Extract a list of properties, by matching parameter name with a given
	 * prefix. The prefix is removed from the param name.
	 * 
	 * @return null if prefix is null.
	 */
	public Properties extractByPrefix(String prefix) {
		if (prefix == null)
			return null;
		int l = prefix.length();
		Properties p = new Properties();
		for (String param : sortedKeys) {
			if (param.startsWith(prefix)) {
				String subParam = param.substring(l);
				p.setProperty(subParam, getProperty(param));
			}
		}
		return p;
	}
}
