package plague_simulator.message;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Variable {
    private final @EqualsAndHashCode.Include String name;
    private final Object value;
    private final Class<?> type;

    static public <T> Variable newVariable(String name, T value, Class<? extends T> type) {
        return new Variable(name, value, type);
    }

    static public Set<Variable> newVariables(Variable... newVariables) {
        return newVariables(new HashSet<>(), newVariables);
    }

    static public Set<Variable> newVariables(Set<Variable> variableSet, Variable... newVariables) {
        Collections.addAll(variableSet, newVariables);
        return variableSet;
    }

    @SafeVarargs
    static public Set<Variable> combineVariables(Set<Variable>... variableSets) {
        Set<Variable> newVariableSet = newVariables();

        for (Set<Variable> variableSet : variableSets) {
            newVariableSet.addAll(variableSet);
        }

        return newVariableSet;
    }


    // region: newVariables multiple args
    static public <T1> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1
    ) {
        return newVariables(
            newVariable(name1, value1, type1)
        );
    }
    static public <T1, T2> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2)
        );
    }
    static public <T1, T2, T3> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2,
        String name3, T3 value3, Class<? extends T3> type3
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2),
            newVariable(name3, value3, type3)
        );
    }
    static public <T1, T2, T3, T4> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2,
        String name3, T3 value3, Class<? extends T3> type3,
        String name4, T4 value4, Class<? extends T4> type4
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2),
            newVariable(name3, value3, type3),
            newVariable(name4, value4, type4)
        );
    }
    static public <T1, T2, T3, T4, T5> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2,
        String name3, T3 value3, Class<? extends T3> type3,
        String name4, T4 value4, Class<? extends T4> type4,
        String name5, T5 value5, Class<? extends T5> type5
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2),
            newVariable(name3, value3, type3),
            newVariable(name4, value4, type4),
            newVariable(name5, value5, type5)
        );
    }
    static public <T1, T2, T3, T4, T5, T6> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2,
        String name3, T3 value3, Class<? extends T3> type3,
        String name4, T4 value4, Class<? extends T4> type4,
        String name5, T5 value5, Class<? extends T5> type5,
        String name6, T6 value6, Class<? extends T6> type6
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2),
            newVariable(name3, value3, type3),
            newVariable(name4, value4, type4),
            newVariable(name5, value5, type5),
            newVariable(name6, value6, type6)
        );
    }
    static public <T1, T2, T3, T4, T5, T6, T7> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2,
        String name3, T3 value3, Class<? extends T3> type3,
        String name4, T4 value4, Class<? extends T4> type4,
        String name5, T5 value5, Class<? extends T5> type5,
        String name6, T6 value6, Class<? extends T6> type6,
        String name7, T7 value7, Class<? extends T7> type7
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2),
            newVariable(name3, value3, type3),
            newVariable(name4, value4, type4),
            newVariable(name5, value5, type5),
            newVariable(name6, value6, type6),
            newVariable(name7, value7, type7)
        );
    }
    static public <T1, T2, T3, T4, T5, T6, T7, T8> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2,
        String name3, T3 value3, Class<? extends T3> type3,
        String name4, T4 value4, Class<? extends T4> type4,
        String name5, T5 value5, Class<? extends T5> type5,
        String name6, T6 value6, Class<? extends T6> type6,
        String name7, T7 value7, Class<? extends T7> type7,
        String name8, T8 value8, Class<? extends T8> type8
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2),
            newVariable(name3, value3, type3),
            newVariable(name4, value4, type4),
            newVariable(name5, value5, type5),
            newVariable(name6, value6, type6),
            newVariable(name7, value7, type7),
            newVariable(name8, value8, type8)
        );
    }
    static public <T1, T2, T3, T4, T5, T6, T7, T8, T9> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2,
        String name3, T3 value3, Class<? extends T3> type3,
        String name4, T4 value4, Class<? extends T4> type4,
        String name5, T5 value5, Class<? extends T5> type5,
        String name6, T6 value6, Class<? extends T6> type6,
        String name7, T7 value7, Class<? extends T7> type7,
        String name8, T8 value8, Class<? extends T8> type8,
        String name9, T9 value9, Class<? extends T9> type9
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2),
            newVariable(name3, value3, type3),
            newVariable(name4, value4, type4),
            newVariable(name5, value5, type5),
            newVariable(name6, value6, type6),
            newVariable(name7, value7, type7),
            newVariable(name8, value8, type8),
            newVariable(name9, value9, type9)
        );
    }
    static public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Set<Variable> newVariables(
        String name1, T1 value1, Class<? extends T1> type1,
        String name2, T2 value2, Class<? extends T2> type2,
        String name3, T3 value3, Class<? extends T3> type3,
        String name4, T4 value4, Class<? extends T4> type4,
        String name5, T5 value5, Class<? extends T5> type5,
        String name6, T6 value6, Class<? extends T6> type6,
        String name7, T7 value7, Class<? extends T7> type7,
        String name8, T8 value8, Class<? extends T8> type8,
        String name9, T9 value9, Class<? extends T9> type9,
        String name10, T10 value10, Class<? extends T10> type10
    ) {
        return newVariables(
            newVariable(name1, value1, type1),
            newVariable(name2, value2, type2),
            newVariable(name3, value3, type3),
            newVariable(name4, value4, type4),
            newVariable(name5, value5, type5),
            newVariable(name6, value6, type6),
            newVariable(name7, value7, type7),
            newVariable(name8, value8, type8),
            newVariable(name9, value9, type9),
            newVariable(name10, value10, type10)
        );
    }

    static public Variable newStringVariable(String name, String value) {
        return newVariable(name, value, String.class);
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1
    ) {
        return newVariables(
            newStringVariable(name1, value1)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,
        String name2, String value2
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,
        String name2, String value2,
        String name3, String value3
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2),
            newStringVariable(name3, value3)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,
        String name2, String value2,
        String name3, String value3,
        String name4, String value4
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2),
            newStringVariable(name3, value3),
            newStringVariable(name4, value4)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,
        String name2, String value2,
        String name3, String value3,
        String name4, String value4,
        String name5, String value5
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2),
            newStringVariable(name3, value3),
            newStringVariable(name4, value4),
            newStringVariable(name5, value5)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,

        String name2, String value2,
        String name3, String value3,
        String name4, String value4,
        String name5, String value5,
        String name6, String value6
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2),
            newStringVariable(name3, value3),
            newStringVariable(name4, value4),
            newStringVariable(name5, value5),
            newStringVariable(name6, value6)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,
        String name2, String value2,
        String name3, String value3,
        String name4, String value4,
        String name5, String value5,
        String name6, String value6,
        String name7, String value7
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2),
            newStringVariable(name3, value3),
            newStringVariable(name4, value4),
            newStringVariable(name5, value5),
            newStringVariable(name6, value6),
            newStringVariable(name7, value7)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,
        String name2, String value2,
        String name3, String value3,
        String name4, String value4,
        String name5, String value5,
        String name6, String value6,
        String name7, String value7,
        String name8, String value8
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2),
            newStringVariable(name3, value3),
            newStringVariable(name4, value4),
            newStringVariable(name5, value5),
            newStringVariable(name6, value6),
            newStringVariable(name7, value7),
            newStringVariable(name8, value8)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,
        String name2, String value2,
        String name3, String value3,
        String name4, String value4,
        String name5, String value5,
        String name6, String value6,
        String name7, String value7,
        String name8, String value8,
        String name9, String value9
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2),
            newStringVariable(name3, value3),
            newStringVariable(name4, value4),
            newStringVariable(name5, value5),
            newStringVariable(name6, value6),
            newStringVariable(name7, value7),
            newStringVariable(name8, value8),
            newStringVariable(name9, value9)
        );
    }
    static public Set<Variable> newStringVariables(
        String name1, String value1,
        String name2, String value2,
        String name3, String value3,
        String name4, String value4,
        String name5, String value5,
        String name6, String value6,
        String name7, String value7,
        String name8, String value8,
        String name9, String value9,
        String name10, String value10
    ) {
        return newVariables(
            newStringVariable(name1, value1),
            newStringVariable(name2, value2),
            newStringVariable(name3, value3),
            newStringVariable(name4, value4),
            newStringVariable(name5, value5),
            newStringVariable(name6, value6),
            newStringVariable(name7, value7),
            newStringVariable(name8, value8),
            newStringVariable(name9, value9),
            newStringVariable(name10, value10)
        );
    }

    static public Variable newIntegerVariable(String name, Integer value) {
        return newVariable(name, value, Integer.class);
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1
    ) {
        return newVariables(
            newIntegerVariable(name1, value1)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2,
        String name3, Integer value3
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2),
            newIntegerVariable(name3, value3)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2,
        String name3, Integer value3,
        String name4, Integer value4
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2),
            newIntegerVariable(name3, value3),
            newIntegerVariable(name4, value4)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2,
        String name3, Integer value3,
        String name4, Integer value4,
        String name5, Integer value5
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2),
            newIntegerVariable(name3, value3),
            newIntegerVariable(name4, value4),
            newIntegerVariable(name5, value5)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2,
        String name3, Integer value3,
        String name4, Integer value4,
        String name5, Integer value5,
        String name6, Integer value6
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2),
            newIntegerVariable(name3, value3),
            newIntegerVariable(name4, value4),
            newIntegerVariable(name5, value5),
            newIntegerVariable(name6, value6)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2,
        String name3, Integer value3,
        String name4, Integer value4,
        String name5, Integer value5,
        String name6, Integer value6,
        String name7, Integer value7
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2),
            newIntegerVariable(name3, value3),
            newIntegerVariable(name4, value4),
            newIntegerVariable(name5, value5),
            newIntegerVariable(name6, value6),
            newIntegerVariable(name7, value7)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2,
        String name3, Integer value3,
        String name4, Integer value4,
        String name5, Integer value5,
        String name6, Integer value6,
        String name7, Integer value7,
        String name8, Integer value8
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2),
            newIntegerVariable(name3, value3),
            newIntegerVariable(name4, value4),
            newIntegerVariable(name5, value5),
            newIntegerVariable(name6, value6),
            newIntegerVariable(name7, value7),
            newIntegerVariable(name8, value8)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2,
        String name3, Integer value3,
        String name4, Integer value4,
        String name5, Integer value5,
        String name6, Integer value6,
        String name7, Integer value7,
        String name8, Integer value8,
        String name9, Integer value9
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2),
            newIntegerVariable(name3, value3),
            newIntegerVariable(name4, value4),
            newIntegerVariable(name5, value5),
            newIntegerVariable(name6, value6),
            newIntegerVariable(name7, value7),
            newIntegerVariable(name8, value8),
            newIntegerVariable(name9, value9)
        );
    }
    static public Set<Variable> newIntegerVariables(
        String name1, Integer value1,
        String name2, Integer value2,
        String name3, Integer value3,
        String name4, Integer value4,
        String name5, Integer value5,
        String name6, Integer value6,
        String name7, Integer value7,
        String name8, Integer value8,
        String name9, Integer value9,
        String name10, Integer value10
    ) {
        return newVariables(
            newIntegerVariable(name1, value1),
            newIntegerVariable(name2, value2),
            newIntegerVariable(name3, value3),
            newIntegerVariable(name4, value4),
            newIntegerVariable(name5, value5),
            newIntegerVariable(name6, value6),
            newIntegerVariable(name7, value7),
            newIntegerVariable(name8, value8),
            newIntegerVariable(name9, value9),
            newIntegerVariable(name10, value10)
        );
    }

    static public Variable newLongVariable(String name, Long value) {
        return newVariable(name, value, Long.class);
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1
    ) {
        return newVariables(
            newLongVariable(name1, value1)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2,
        String name3, Long value3
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2),
            newLongVariable(name3, value3)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2,
        String name3, Long value3,
        String name4, Long value4
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2),
            newLongVariable(name3, value3),
            newLongVariable(name4, value4)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2,
        String name3, Long value3,
        String name4, Long value4,
        String name5, Long value5
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2),
            newLongVariable(name3, value3),
            newLongVariable(name4, value4),
            newLongVariable(name5, value5)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2,
        String name3, Long value3,
        String name4, Long value4,
        String name5, Long value5,
        String name6, Long value6
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2),
            newLongVariable(name3, value3),
            newLongVariable(name4, value4),
            newLongVariable(name5, value5),
            newLongVariable(name6, value6)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2,
        String name3, Long value3,
        String name4, Long value4,
        String name5, Long value5,
        String name6, Long value6,
        String name7, Long value7
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2),
            newLongVariable(name3, value3),
            newLongVariable(name4, value4),
            newLongVariable(name5, value5),
            newLongVariable(name6, value6),
            newLongVariable(name7, value7)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2,
        String name3, Long value3,
        String name4, Long value4,
        String name5, Long value5,
        String name6, Long value6,
        String name7, Long value7,
        String name8, Long value8
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2),
            newLongVariable(name3, value3),
            newLongVariable(name4, value4),
            newLongVariable(name5, value5),
            newLongVariable(name6, value6),
            newLongVariable(name7, value7),
            newLongVariable(name8, value8)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2,
        String name3, Long value3,
        String name4, Long value4,
        String name5, Long value5,
        String name6, Long value6,
        String name7, Long value7,
        String name8, Long value8,
        String name9, Long value9
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2),
            newLongVariable(name3, value3),
            newLongVariable(name4, value4),
            newLongVariable(name5, value5),
            newLongVariable(name6, value6),
            newLongVariable(name7, value7),
            newLongVariable(name8, value8),
            newLongVariable(name9, value9)
        );
    }
    static public Set<Variable> newLongVariables(
        String name1, Long value1,
        String name2, Long value2,
        String name3, Long value3,
        String name4, Long value4,
        String name5, Long value5,
        String name6, Long value6,
        String name7, Long value7,
        String name8, Long value8,
        String name9, Long value9,
        String name10, Long value10
    ) {
        return newVariables(
            newLongVariable(name1, value1),
            newLongVariable(name2, value2),
            newLongVariable(name3, value3),
            newLongVariable(name4, value4),
            newLongVariable(name5, value5),
            newLongVariable(name6, value6),
            newLongVariable(name7, value7),
            newLongVariable(name8, value8),
            newLongVariable(name9, value9),
            newLongVariable(name10, value10)
        );
    }

    static public Variable newDoubleVariable(String name, Double value) {
        return newVariable(name, value, Double.class);
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1
    ) {
        return newVariables(
            newDoubleVariable(name1, value1)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2,
        String name3, Double value3
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2),
            newDoubleVariable(name3, value3)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2,
        String name3, Double value3,
        String name4, Double value4
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2),
            newDoubleVariable(name3, value3),
            newDoubleVariable(name4, value4)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2,
        String name3, Double value3,
        String name4, Double value4,
        String name5, Double value5
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2),
            newDoubleVariable(name3, value3),
            newDoubleVariable(name4, value4),
            newDoubleVariable(name5, value5)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2,
        String name3, Double value3,
        String name4, Double value4,
        String name5, Double value5,
        String name6, Double value6
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2),
            newDoubleVariable(name3, value3),
            newDoubleVariable(name4, value4),
            newDoubleVariable(name5, value5),
            newDoubleVariable(name6, value6)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2,
        String name3, Double value3,
        String name4, Double value4,
        String name5, Double value5,
        String name6, Double value6,
        String name7, Double value7
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2),
            newDoubleVariable(name3, value3),
            newDoubleVariable(name4, value4),
            newDoubleVariable(name5, value5),
            newDoubleVariable(name6, value6),
            newDoubleVariable(name7, value7)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2,
        String name3, Double value3,
        String name4, Double value4,
        String name5, Double value5,
        String name6, Double value6,
        String name7, Double value7,
        String name8, Double value8
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2),
            newDoubleVariable(name3, value3),
            newDoubleVariable(name4, value4),
            newDoubleVariable(name5, value5),
            newDoubleVariable(name6, value6),
            newDoubleVariable(name7, value7),
            newDoubleVariable(name8, value8)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2,
        String name3, Double value3,
        String name4, Double value4,
        String name5, Double value5,
        String name6, Double value6,
        String name7, Double value7,
        String name8, Double value8,
        String name9, Double value9
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2),
            newDoubleVariable(name3, value3),
            newDoubleVariable(name4, value4),
            newDoubleVariable(name5, value5),
            newDoubleVariable(name6, value6),
            newDoubleVariable(name7, value7),
            newDoubleVariable(name8, value8),
            newDoubleVariable(name9, value9)
        );
    }
    static public Set<Variable> newDoubleVariables(
        String name1, Double value1,
        String name2, Double value2,
        String name3, Double value3,
        String name4, Double value4,
        String name5, Double value5,
        String name6, Double value6,
        String name7, Double value7,
        String name8, Double value8,
        String name9, Double value9,
        String name10, Double value10
    ) {
        return newVariables(
            newDoubleVariable(name1, value1),
            newDoubleVariable(name2, value2),
            newDoubleVariable(name3, value3),
            newDoubleVariable(name4, value4),
            newDoubleVariable(name5, value5),
            newDoubleVariable(name6, value6),
            newDoubleVariable(name7, value7),
            newDoubleVariable(name8, value8),
            newDoubleVariable(name9, value9),
            newDoubleVariable(name10, value10)
        );
    }

    static public Variable newFloatVariable(String name, Float value) {
        return newVariable(name, value, Float.class);
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1
    ) {
        return newVariables(
            newFloatVariable(name1, value1)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2,
        String name3, Float value3
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2),
            newFloatVariable(name3, value3)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2,
        String name3, Float value3,
        String name4, Float value4
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2),
            newFloatVariable(name3, value3),
            newFloatVariable(name4, value4)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2,
        String name3, Float value3,
        String name4, Float value4,
        String name5, Float value5
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2),
            newFloatVariable(name3, value3),
            newFloatVariable(name4, value4),
            newFloatVariable(name5, value5)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2,
        String name3, Float value3,
        String name4, Float value4,
        String name5, Float value5,
        String name6, Float value6
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2),
            newFloatVariable(name3, value3),
            newFloatVariable(name4, value4),
            newFloatVariable(name5, value5),
            newFloatVariable(name6, value6)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2,
        String name3, Float value3,
        String name4, Float value4,
        String name5, Float value5,
        String name6, Float value6,
        String name7, Float value7
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2),
            newFloatVariable(name3, value3),
            newFloatVariable(name4, value4),
            newFloatVariable(name5, value5),
            newFloatVariable(name6, value6),
            newFloatVariable(name7, value7)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2,
        String name3, Float value3,
        String name4, Float value4,
        String name5, Float value5,
        String name6, Float value6,
        String name7, Float value7,
        String name8, Float value8
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2),
            newFloatVariable(name3, value3),
            newFloatVariable(name4, value4),
            newFloatVariable(name5, value5),
            newFloatVariable(name6, value6),
            newFloatVariable(name7, value7),
            newFloatVariable(name8, value8)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2,
        String name3, Float value3,
        String name4, Float value4,
        String name5, Float value5,
        String name6, Float value6,
        String name7, Float value7,
        String name8, Float value8,
        String name9, Float value9
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2),
            newFloatVariable(name3, value3),
            newFloatVariable(name4, value4),
            newFloatVariable(name5, value5),
            newFloatVariable(name6, value6),
            newFloatVariable(name7, value7),
            newFloatVariable(name8, value8),
            newFloatVariable(name9, value9)
        );
    }
    static public Set<Variable> newFloatVariables(
        String name1, Float value1,
        String name2, Float value2,
        String name3, Float value3,
        String name4, Float value4,
        String name5, Float value5,
        String name6, Float value6,
        String name7, Float value7,
        String name8, Float value8,
        String name9, Float value9,
        String name10, Float value10
    ) {
        return newVariables(
            newFloatVariable(name1, value1),
            newFloatVariable(name2, value2),
            newFloatVariable(name3, value3),
            newFloatVariable(name4, value4),
            newFloatVariable(name5, value5),
            newFloatVariable(name6, value6),
            newFloatVariable(name7, value7),
            newFloatVariable(name8, value8),
            newFloatVariable(name9, value9),
            newFloatVariable(name10, value10)
        );
    }

    static public Variable newBooleanVariable(String name, Boolean value) {
        return newVariable(name, value, Boolean.class);
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1
    ) {
        return newVariables(
            newBooleanVariable(name1, value1)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2,
        String name3, Boolean value3
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2),
            newBooleanVariable(name3, value3)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2,
        String name3, Boolean value3,
        String name4, Boolean value4
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2),
            newBooleanVariable(name3, value3),
            newBooleanVariable(name4, value4)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2,
        String name3, Boolean value3,
        String name4, Boolean value4,
        String name5, Boolean value5
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2),
            newBooleanVariable(name3, value3),
            newBooleanVariable(name4, value4),
            newBooleanVariable(name5, value5)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2,
        String name3, Boolean value3,
        String name4, Boolean value4,
        String name5, Boolean value5,
        String name6, Boolean value6
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2),
            newBooleanVariable(name3, value3),
            newBooleanVariable(name4, value4),
            newBooleanVariable(name5, value5),
            newBooleanVariable(name6, value6)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2,
        String name3, Boolean value3,
        String name4, Boolean value4,
        String name5, Boolean value5,
        String name6, Boolean value6,
        String name7, Boolean value7
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2),
            newBooleanVariable(name3, value3),
            newBooleanVariable(name4, value4),
            newBooleanVariable(name5, value5),
            newBooleanVariable(name6, value6),
            newBooleanVariable(name7, value7)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2,
        String name3, Boolean value3,
        String name4, Boolean value4,
        String name5, Boolean value5,
        String name6, Boolean value6,
        String name7, Boolean value7,
        String name8, Boolean value8
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2),
            newBooleanVariable(name3, value3),
            newBooleanVariable(name4, value4),
            newBooleanVariable(name5, value5),
            newBooleanVariable(name6, value6),
            newBooleanVariable(name7, value7),
            newBooleanVariable(name8, value8)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2,
        String name3, Boolean value3,
        String name4, Boolean value4,
        String name5, Boolean value5,
        String name6, Boolean value6,
        String name7, Boolean value7,
        String name8, Boolean value8,
        String name9, Boolean value9
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2),
            newBooleanVariable(name3, value3),
            newBooleanVariable(name4, value4),
            newBooleanVariable(name5, value5),
            newBooleanVariable(name6, value6),
            newBooleanVariable(name7, value7),
            newBooleanVariable(name8, value8),
            newBooleanVariable(name9, value9)
        );
    }
    static public Set<Variable> newBooleanVariables(
        String name1, Boolean value1,
        String name2, Boolean value2,
        String name3, Boolean value3,
        String name4, Boolean value4,
        String name5, Boolean value5,
        String name6, Boolean value6,
        String name7, Boolean value7,
        String name8, Boolean value8,
        String name9, Boolean value9,
        String name10, Boolean value10
    ) {
        return newVariables(
            newBooleanVariable(name1, value1),
            newBooleanVariable(name2, value2),
            newBooleanVariable(name3, value3),
            newBooleanVariable(name4, value4),
            newBooleanVariable(name5, value5),
            newBooleanVariable(name6, value6),
            newBooleanVariable(name7, value7),
            newBooleanVariable(name8, value8),
            newBooleanVariable(name9, value9),
            newBooleanVariable(name10, value10)
        );
    }
    // endregion
}
