package com.jstarcraft.ai.model;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.model.ModelCycle;

/**
 * 模仿复杂对象
 * 
 * @author Birdy
 *
 */
public class MockComplexObject implements ModelCycle {

	private Integer id;

	private String firstName;

	private String lastName;

	private String[] names;

	private int money;

	private int[] currencies;

	private Instant instant;

	private MockEnumeration race;

	private Type type;

	private LinkedList<MockSimpleObject> list;

	private HashMap<Integer, MockSimpleObject> map;

	private MockComplexObject() {
	}

	public Integer getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getMoney() {
		return money;
	}

	public Instant getInstant() {
		return instant;
	}

	public MockEnumeration getRace() {
		return race;
	}

	public String[] toNames() {
		return names;
	}

	public int[] toCurrencies() {
		return currencies;
	}

	@Override
	public void beforeSave() {
		names = null;
		currencies = null;
	}

	@Override
	public void afterLoad() {
		names = new String[] { firstName, lastName };
		currencies = new int[] { money };
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		MockComplexObject that = (MockComplexObject) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.id, that.id);
		equal.append(this.firstName, that.firstName);
		equal.append(this.lastName, that.lastName);
		equal.append(this.money, that.money);
		equal.append(this.instant, that.instant);
		equal.append(this.race, that.race);
		equal.append(this.type, that.type);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(id);
		hash.append(firstName);
		hash.append(lastName);
		hash.append(money);
		hash.append(instant);
		hash.append(race);
		hash.append(type);
		return hash.toHashCode();
	}

	public static MockComplexObject valueOf(Integer id, String firstName, String lastName, int money, Instant instant, MockEnumeration race) {
		MockComplexObject value = new MockComplexObject();
		value.id = id;
		value.firstName = firstName;
		value.lastName = lastName;
		value.names = new String[] { firstName, lastName };
		value.money = money;
		value.currencies = new int[] { money };
		value.instant = instant;
		value.race = race;
		value.list = new LinkedList<>();
		value.map = new HashMap<>();
		value.type = MockComplexObject.class;
		for (int index = 0; index < money; index++) {
			MockSimpleObject object = MockSimpleObject.valueOf(index, lastName);
			value.list.add(object);
			value.map.put(index, object);
		}
		return value;
	}

}
