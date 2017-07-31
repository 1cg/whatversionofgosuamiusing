package app.view;

import app.Server;
import app.model.Application;
import app.model.Release;
import app.model.Resource;
import app.model.Version;
import bb.runtime.BaseBBTemplate;
import bb.sparkjava.BBSparkTemplate;

import java.util.List;
import java.util.function.Function;

public class AppTemplate extends BBSparkTemplate {

    public String currentPath() {
        return Server.fixPath(getRequest());
    }

    public <T> Object listOptions(List<T> items, T selected) {
        return listOptions(items, Object::toString, Object::toString, selected);
    }

    public <T> Object listOptions(List<T> items, Function<T, Object> toStr, Function<T, Object> toVal, T selected) {
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
        return raw(sb.toString());
    }

    public Object breadcrumb(Application selectedApp, Version selectedVersion, Release selectedRelease, String path) {
        StringBuilder sb = new StringBuilder("<ol class=\"breadcrumb\">\n" +
                "    <li><a href=\"/\">Home</a></li>\n");
        sb.append("    <li><a href=\"").append(pathFor(selectedApp)).append("\" class=\"breadcrumb-item\">").append(selectedApp.getName()).append("</a></li>\n");
        sb.append("    <li><a href=\"").append(pathFor(selectedApp, selectedVersion)).append("\" class=\"breadcrumb-item\">").append(selectedVersion.getName()).append("</a></li>\n");
        sb.append("    <li><a href=\"").append(pathFor(selectedApp, selectedVersion, selectedRelease))
                .append("\" class=\"breadcrumb-item");
        if (path.length() == 0) {
            sb.append(" active");
        }
        sb.append("\">").append(selectedRelease.getName()).append("</a></li>\n");

        if (path.length() > 0) {
            String[] pathArray = path.split("/");
            int i = 0;
            boolean zipped = false;
            StringBuilder pathBuilder = new StringBuilder();
            while (i < pathArray.length) {
                String currentResource = pathArray[i];
                if (zipped) {
                    i += 1;
                    while (i < pathArray.length && !(currentResource.endsWith(".zip") || currentResource.endsWith(".jar"))) {
                        currentResource = currentResource + "/" + pathArray[i++];
                    }
                    i -= 1;
                }
                if (currentResource.endsWith(".zip") || currentResource.endsWith(".jar")) {
                    zipped = true;
                }
                pathBuilder.append(currentResource).append("/");
                i += 1;
                sb.append("    <li><a href=\"").append(pathFor(selectedApp, selectedVersion, selectedRelease)).append(pathBuilder).append("\" class=\"breadcrumb-item");
                if (i == pathArray.length) {
                    sb.append(" active");
                }
                sb.append("\">").append(currentResource).append("</a></li>");
            }
        }
        sb.append("\n</ol>\n");
        return raw(sb.toString());
    }

    public String pathFor(Application selectedApp) {
        return "/" + selectedApp.getFileSystemName();
    }

    public String pathFor(Application selectedApp, Version selectedVersion) {
        return pathFor(selectedApp) + "/" + selectedVersion.getName();
    }

    public String pathFor(Application selectedApp, Version selectedVersion, Release selectedRelease) {
        return pathFor(selectedApp, selectedVersion) + "/" + selectedRelease.getName() + "/";
    }

    public String pathFor(Application selectedApp, Version selectedVersion, Release selectedRelease, String path) {
            return pathFor(selectedApp, selectedVersion, selectedRelease) + path + "/" ;
    }

    public String pathFor(Application selectedApp, Version selectedVersion, Release selectedRelease, Resource selectedResource, String path) {
        if (path != null && path.length() > 0) {
            return pathFor(selectedApp, selectedVersion, selectedRelease, path) + selectedResource.getName() + "/";
        } else {
            return pathFor(selectedApp, selectedVersion, selectedRelease) + selectedResource.getName() + "/";
        }
    }



}
