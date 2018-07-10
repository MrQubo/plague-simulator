package plague_simulator.message;

import java.util.Collection;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;

import lombok.AccessLevel;
import lombok.Getter;

import plague_simulator.message.MessageInterpolator;
import plague_simulator.message.Variable;

public class ELMessageInterpolator implements MessageInterpolator {
    @Getter(AccessLevel.PRIVATE)
    static private final ExpressionFactory expressionFactory = ExpressionFactory.newInstance();


    @Override
    public String interpolate(String template, Collection<? extends Variable> variables) {
        final ELContext context = createContext(variables);
        return (String) createValueExpression(template, context).getValue(context);
    }


    public ValueExpression createValueExpression(String template, ELContext context) {
        return getExpressionFactory().createValueExpression(context, template, String.class);
    }


    private ValueExpression toValueExpression(Variable variable) {
        return getExpressionFactory().createValueExpression(variable.getValue(), variable.getType());
    }

    private ELContext createContext(Collection<? extends Variable> variables) {
        final ELContext context = newContext();

        for (Variable variable : variables) {
            context.getVariableMapper().setVariable(variable.getName(), toValueExpression(variable));
        }

        return context;
    }

    private ELContext newContext() {
        return new StandardELContext(getExpressionFactory());
    }
}
