package com.fun.yzss.aop;

/**
 * Created by fanqq on 2016/7/15.
 */

/**
 * What happens when multiple pieces of advice all want to run at the same join point?
 * Spring AOP follows the same precedence rules as AspectJ to determine the order of advice execution.
 * The highest precedence advice runs first "on the way in" (so given two pieces of before advice,
 * the one with highest precedence runs first).
 * "On the way out" from a join point, the highest precedence advice runs last
 * (so given two pieces of after advice, the one with the highest precedence will run second).
 */
public class AopOrder {
    public static int Exception = 0;
    public static int DesAop = 3;
    public static int TransactionAop = 5;
}
