<%@ layout Layout %>
<%@ import app.model.* %>
<%@ extends app.view.AppTemplate %>
<%@ params(Application selectedApp, String selectedVersion) %>

<h1 class="text-center">What Version of Gosu Am I Using?</h1>

<form>
    <div class="form-group">
        <label class="control-label">
            Select Application <i id="version-ind" class="fa fa-spinner fa-spin" style="display:none"></i>
        </label>
        <select class="form-control" name="app" ic-get-from="/" ic-target="#versions" ic-indicator="#version-ind">
            <option value="NO APP">Select an Application</option>
            ${listOptions(Application.getAll(), Application::getName, Application::getFileSystemName, selectedApp)}
        </select>
    </div>

    <div id="versions" class="form-group">
        <% if(selectedApp != null) { %>
        <%@ section Versions(selectedApp, selectedVersion) %>
        <label class="control-label">
            Select Version <i id="release-ind" class="fa fa-spinner fa-spin" style="display:none"></i>
        </label>

        <select class="form-control" name="version" ic-get-from="/${selectedApp.getFileSystemName()}" ic-target="#releases"
                ic-include='{"app":"${selectedApp.getFileSystemName()}"}' ic-indicator="#release-ind">
            <option value="">Select a Major Version</option>
            ${listOptions(selectedApp.getVersionsNames(), selectedVersion)}
        </select>

        <div id="releases">
            <% if(selectedVersion != null) { %>
            <%@ section Releases(selectedApp, selectedVersion) %>
            <h2>RELEASES</h2>

            <table class="table">
                <thead>
                <tr>
                    <th>
                        Release
                    </th>
                    <th>
                        Gosu Version
                    </th>
                    <th>
                    </th>
                </tr>
                </thead>
                <tbody>
                <% for(String release: selectedApp.getReleasesNames(selectedVersion)) { %>
                <tr>
                    <td>${release}</td>
                    <td>Unknown</td>
                    <td>
                        <a href='/${selectedApp.getFileSystemName() + "/" + selectedVersion + "/" + release + "/"}'>Explore >>></a>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <%@ end section %>
            <% } %>
        </div>
        <%@ end section %>
        <% } %>
    </div>

</form>
