package app.view;

import app.model.Application;
import bb.runtime.BaseBBTemplate;

import java.util.List;
import java.util.function.Function;

public class AppTemplate extends BaseBBTemplate {

    public <T> String listOptions(List<T> items, T selected) {
        return listOptions(items, Object::toString, Object::toString, selected);
    }

    public <T> String listOptions(List<T> items, Function<T, Object> toStr, Function<T, Object> toVal, T selected) {
        StringBuilder sb = new StringBuilder();
        for (T item : items) {
            sb.append("<option value=\"")
                    .append(toVal.apply(item))
                    .append('"');
            if (item.equals(selected)) {
                sb.append(" selected");
            }
            sb.append(">").append(toStr.apply(item)).append("</option>");
        }
        return sb.toString();
    }



}
