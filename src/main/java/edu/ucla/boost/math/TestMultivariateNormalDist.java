package edu.ucla.boost.math;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.apache.commons.math3.distribution.*;

import edu.ucla.boost.common.TimeUtil;

public class TestMultivariateNormalDist {

	public static void printPrimitiveArray(Object aObject) {
		if (aObject.getClass().isArray()) {
			if (aObject instanceof Object[]) // can we cast to Object[]
				System.out.println(Arrays.toString((Object[]) aObject));
			else {  // we can't cast to Object[] - case of primitive arrays
				int length = Array.getLength(aObject);
				Object[] objArr = new Object[length];
				for (int i=0; i<length; i++)
					objArr[i] =  Array.get(aObject, i);
				System.out.println(Arrays.toString(objArr));
			}
		}
	}


	public static void main(String[] args) {
		NormalDistribution dist = new NormalDistribution();
		//printPrimitiveArray(dist.sample(10000));

		double[][] coVarMatrix = {{1,0,0}, {0,1,0}, {0,0,1}};
		double[] meanVector = {0,0,0};

		MultivariateNormalDistribution multiDist = new MultivariateNormalDistribution(meanVector, coVarMatrix);

		TimeUtil.start();
		multiDist.sample(10000000);
		/*
		for (int i=0; i<100000; i++) {
			printPrimitiveArray(multiDist.sample());
		}*/
		TimeUtil.printPassed();
	}
}
