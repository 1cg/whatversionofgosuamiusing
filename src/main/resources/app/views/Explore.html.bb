<%@ layout Layout %>
<%@ import app.model.* %>
<%@ extends app.view.AppTemplate %>

<%@ params(Application selectedApp, Version selectedVersion, Release release, Resource selectedResource, String path, String filter) %>

${breadcrumb(selectedApp, selectedVersion, release, path)}

<hr/>

<h2>Explore Release</h2>

<% String newVersionNumber = null; %>
<div class="row">
    <div class="col-md-6">
        <%@ section exploreForm(selectedApp, selectedVersion, release, newVersionNumber) %>
            <form ic-post-to="${pathFor(selectedApp, selectedVersion, release)}">
                <label for="version">Gosu Version Info</label>
                <input type="text" class="form-control mb-2 mr-sm-2 mb-sm-0" name="version" id="version"
                       <% if (release.getGosuVersionInfo().equals("No Info")) { %>
                       placeholder="Enter Gosu Versions"
                       <% } else { %>
                       placeholder="Update Gosu Version from <%= release.getGosuVersionInfo() %>"
                       <% } %>
                >
                <button type="submit" class="btn btn-primary" >Save</button>
            </form>
            <% if (newVersionNumber != null) { %>
                <div class="alert alert-success" role="alert">
                  Gosu Version for ${nameFor(selectedApp, release)}
                  successfully updated to ${newVersionNumber}
                </div>
            <% } %>
        <%@ end section %>
    </div>
</div>

<hr/>

<% if (selectedResource.isDirectory()) { %>
<div class="row">
    <div class="col-md-12">
        <h3>
            Search Resources
            <span id="indicator" style="display:none"> <i class="fa fa-spinner fa-spin"></i> Searching... </span>
        </h3>

        <input class="form-control" type="text" name="filter" placeholder="Search Resources"
               value="${filter}"
               ic-get-from='${pathFor(selectedApp, selectedVersion, release, path)}'
               ic-trigger-on="keyup changed" ic-trigger-delay="500ms"
               ic-target="#resource-list" ic-indicator="#indicator">
    </div>
</div>

<hr/>
<% } %>

<div id="resource-list">
    <%@ section ResourceList(selectedApp, selectedVersion, release, selectedResource, path, filter) %>
    <% if (selectedResource.isUnzipping()) { %>
      <h3>Extracting ${selectedResource.getName()}</h3>
        <div class="progress" ic-trigger-on="load" ic-get-from="${currentPath()}" ic-target="#resource-list" ic-trigger-delay="700ms">
            <div class="progress-bar" role="progressbar" aria-valuenow="${selectedResource.getPercentUnzipped()}"
                 aria-valuemin="0" aria-valuemax="100" style="width: ${selectedResource.getPercentUnzipped()}%;">
                <span class="sr-only">${selectedResource.getPercentUnzipped()}% Complete</span>
            </div>
        </div>
    <% } else if (selectedResource.isDirectory()) { %>
    <div class="list-group" id="explorer">
        <% for (Resource r: selectedResource.getResources(filter)) { %>
        <a class="list-group-item" href="${pathFor(selectedApp, selectedVersion, release, r, path)}">
          ${r.getName()}
          <% if (r.isDirectory()) { %>
            <i class="fa fa-folder-open" aria-hidden="true"></i>
          <% } %>
        </a>
        <% } %>
    </div>
    <% } else { %>
    <h4>File Contents:</h4>
    <pre>${selectedResource.getContent()}</pre>
    <% } %>
   <%@ end section %>
</div>
