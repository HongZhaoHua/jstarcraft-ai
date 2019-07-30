package com.jstarcraft.ai.math;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.UnitNumber;

public class UnitNumberTestCase {

	@Test
	public void testIllegalArgument() {
		try {
			new UnitNumber(1, Float.NaN);
			Assert.fail();
		} catch (IllegalArgumentException exception) {
		}
		try {
			new UnitNumber(1, Float.POSITIVE_INFINITY);
			Assert.fail();
		} catch (IllegalArgumentException exception) {
		}
		try {
			new UnitNumber(1, Float.NEGATIVE_INFINITY);
			Assert.fail();
		} catch (IllegalArgumentException exception) {
		}
	}

	@Test
	public void testCompare() {
		{
			UnitNumber left = new UnitNumber(1, 1F);
			UnitNumber right = new UnitNumber();
			Assert.assertThat(left.compareTo(right), CoreMatchers.equalTo(1));
			Assert.assertThat(right.compareTo(left), CoreMatchers.equalTo(-1));
		}

		{
			UnitNumber left = new UnitNumber(1, -1F);
			UnitNumber right = new UnitNumber();
			Assert.assertThat(left.compareTo(right), CoreMatchers.equalTo(-1));
			Assert.assertThat(right.compareTo(left), CoreMatchers.equalTo(1));
		}

		{
			UnitNumber left = new UnitNumber(0, 0F);
			UnitNumber right = new UnitNumber();
			Assert.assertThat(left.compareTo(right), CoreMatchers.equalTo(0));
			Assert.assertThat(right.compareTo(left), CoreMatchers.equalTo(0));
		}

		{
			UnitNumber left = new UnitNumber(1, 1F);
			UnitNumber right = new UnitNumber(1, -1F);
			Assert.assertThat(left.compareTo(right), CoreMatchers.equalTo(1));
			Assert.assertThat(right.compareTo(left), CoreMatchers.equalTo(-1));
		}
	}

	@Test
	public void testAdd() {
		{
			UnitNumber left = new UnitNumber(5, 1F);
			UnitNumber right = new UnitNumber(0, 1F);
			UnitNumber number = UnitNumber.add(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(left));
			Assert.assertThat(number, CoreMatchers.equalTo(left.add(right)));
		}

		{
			UnitNumber left = new UnitNumber(0, 1F);
			UnitNumber right = new UnitNumber(5, 1F);
			UnitNumber number = UnitNumber.add(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(right));
			Assert.assertThat(number, CoreMatchers.equalTo(left.add(right)));
		}

		{
			UnitNumber left = new UnitNumber(1, 1F);
			UnitNumber right = new UnitNumber();
			UnitNumber number = UnitNumber.add(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(left));
			Assert.assertThat(number, CoreMatchers.equalTo(left.add(right)));
		}

		{
			// 相当于1 kilo
			UnitNumber left = new UnitNumber(1, 1F);
			// 相当于0.999 kilo
			UnitNumber right = new UnitNumber(0, 999F);
			UnitNumber number = UnitNumber.add(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(0, 1999F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.add(right)));
		}

		{
			// 相当于1 kilo
			UnitNumber left = new UnitNumber(1, 1F);
			// 相当于1 kilo
			UnitNumber right = new UnitNumber(0, 1000F);
			UnitNumber number = UnitNumber.add(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(1, 2F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.add(right)));
		}

		{
			// 相当于999.999 kilo
			UnitNumber left = new UnitNumber(0, 999999F);
			// 相当于0.001 kilo
			UnitNumber right = new UnitNumber(0, 1F);
			UnitNumber number = UnitNumber.add(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(1, 1000F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.add(right)));
		}
	}

	@Test
	public void testSubtract() {
		{
			UnitNumber left = new UnitNumber(5, 1F);
			UnitNumber right = new UnitNumber(0, 1F);
			UnitNumber number = UnitNumber.subtract(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(left));
			Assert.assertThat(number, CoreMatchers.equalTo(left.subtract(right)));
		}

		{
			UnitNumber left = new UnitNumber(0, 1F);
			UnitNumber right = new UnitNumber(5, 1F);
			UnitNumber number = UnitNumber.subtract(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(5, -1F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.subtract(right)));
		}

		{
			UnitNumber left = new UnitNumber(1, 1F);
			UnitNumber right = new UnitNumber();
			UnitNumber number = UnitNumber.subtract(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(left));
			Assert.assertThat(number, CoreMatchers.equalTo(left.subtract(right)));
		}

		{
			// 相当于1 kilo
			UnitNumber left = new UnitNumber(1, 1F);
			// 相当于0.999 kilo
			UnitNumber right = new UnitNumber(0, 999F);
			UnitNumber number = UnitNumber.subtract(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(0, 1F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.subtract(right)));
		}

		{
			// 相当于1 kilo
			UnitNumber left = new UnitNumber(1, 1F);
			// 相当于1 kilo
			UnitNumber right = new UnitNumber(0, 1000F);
			UnitNumber number = UnitNumber.subtract(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber()));
			Assert.assertThat(number, CoreMatchers.equalTo(left.subtract(right)));
		}

		{
			// 相当于1000 kilo
			UnitNumber left = new UnitNumber(1, 1000F);
			// 相当于0.001 kilo
			UnitNumber right = new UnitNumber(0, 1);
			UnitNumber number = UnitNumber.subtract(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(0, 999999F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.subtract(right)));
		}
	}

	@Test
	public void testMultiply() {
		{
			UnitNumber left = new UnitNumber();
			UnitNumber right = new UnitNumber(1, 1F);
			UnitNumber number = UnitNumber.multiply(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber()));
			Assert.assertThat(number, CoreMatchers.equalTo(left.multiply(right)));
		}

		{
			UnitNumber left = new UnitNumber(1, 1F);
			UnitNumber right = new UnitNumber();
			UnitNumber number = UnitNumber.multiply(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber()));
			Assert.assertThat(number, CoreMatchers.equalTo(left.multiply(right)));
		}

		{
			// 相当于1 kilo
			UnitNumber left = new UnitNumber(1, 1F);
			// 相当于0.5 kilo
			UnitNumber right = new UnitNumber(0, 500F);
			UnitNumber number = UnitNumber.multiply(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(0, 500000F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.multiply(right)));
		}

		{
			// 相当于1 kilo
			UnitNumber left = new UnitNumber(1, 1F);
			// 相当于2 kilo
			UnitNumber right = new UnitNumber(0, 2000F);
			UnitNumber number = UnitNumber.multiply(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(1, 2000F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.multiply(right)));
		}

		{
			// 相当于999.999 kilo
			UnitNumber left = new UnitNumber(0, 999999F);
			// 相当于0.001 kilo
			UnitNumber right = new UnitNumber(0, 1F);
			UnitNumber number = UnitNumber.multiply(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(1, 999.999F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.multiply(right)));
		}
	}

	@Test
	public void testDivide() {
		{
			UnitNumber left = new UnitNumber();
			UnitNumber right = new UnitNumber(1, 1F);
			UnitNumber number = UnitNumber.divide(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber()));
			Assert.assertThat(number, CoreMatchers.equalTo(left.divide(right)));
		}

		{
			UnitNumber left = new UnitNumber(1, 1F);
			UnitNumber right = new UnitNumber();
			try {
				UnitNumber.divide(left, right);
				Assert.fail();
			} catch (ArithmeticException exception) {
			}
			try {
				left.divide(right);
				Assert.fail();
			} catch (ArithmeticException exception) {
			}
		}

		{
			// 相当于1 kilo
			UnitNumber left = new UnitNumber(1, 1F);
			// 相当于0.5 kilo
			UnitNumber right = new UnitNumber(0, 500F);
			UnitNumber number = UnitNumber.divide(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(0, 2F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.divide(right)));
		}

		{
			// 相当于1 kilo
			UnitNumber left = new UnitNumber(1, 1F);
			// 相当于2 kilo
			UnitNumber right = new UnitNumber(0, 2000F);
			UnitNumber number = UnitNumber.divide(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(1, 0.0005F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.divide(right)));
		}

		{
			// 相当于999.999 kilo
			UnitNumber left = new UnitNumber(0, 999999F);
			// 相当于0.001 kilo
			UnitNumber right = new UnitNumber(0, 1F);
			UnitNumber number = UnitNumber.divide(left, right);
			Assert.assertThat(number, CoreMatchers.equalTo(new UnitNumber(1, 999.999F)));
			Assert.assertThat(number, CoreMatchers.equalTo(left.divide(right)));
		}
	}

}
