<%--
  Created by IntelliJ IDEA.
  User: JanTore
  Date: 16.10.2014
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html >
  <head>
      <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.0/css/bootstrap-combined.min.css">
      <link rel="stylesheet" href="/css/app.css">
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
      <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.0/angular.js"></script>
      <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.0/angular-resource.min.js"></script>
      <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.0/angular-sanitize.js"></script>
      <script src="js/app.js"></script>
      <title>My news feed</title>
  </head>
  <body ng-app="app" ng-controller="AppCtrl">
      <div ng-if="username !== '' && enoughGroups" class="page-container">
          <div class='span3 sidebar'>
              <h2>Interests</h2>
              <ul class="nav nav-tabs nav-stacked">
                  <li ng-repeat="group in interestGroups">
                      <a class="thumbnail" ng-click="$parent.getRecommendations(group.groupId)">
                          <img ng-src="/images/{{group.groupId}}.png">
                      </a>
                  </li>
              </ul>
          </div>

          <div class="container">
              <p>Time: {{responseInfo.time}} | Hits: {{responseInfo.hitsTotal}} | Showing: {{responseInfo.hitsShowing}} | For group {{responseInfo.groupId}}</p>
              <div class="feed-container" ng-repeat="headline in news">
                  <h1><a class="feed-headline" role="button" target="_blank" href="{{headline.link}}">{{createTrustedHtml(headline.title)}}</a></h1>
                  <div ng-bind-html="createTrustedHtml(headline.description)"></div>
                  <div class="feed-footer">
                      <div class="feed-footer-div"><p class="text-left">{{parseDate(headline.pubDate)}} - {{headline.score}}</p></div>
                      <div class="feed-footer-div"><p class="text-right">{{getSource(headline.link)}}</p></div>
                  </div>
              </div>
          </div>
      </div>
      <div ng-if="username === ''" class="jumbotron">
          <div class="container">
              <div class="feed-container">
                  <h1>You are not logged in</h1>
                  <p>You are either not logged in to your chrome extension, has disabled it or do not have it.</p>
                  <p>If you have the extension, please log in and refresh the page.</p>
                  <p>This page is for displaying recommended news articles for users with the InterestRecorder chrome extension only.</p>
                  <p><a class="btn btn-primary btn-lg" href="https://chrome.google.com/webstore/detail/interestrecorder/jkinbeehiiiklikdankjifoeobffbhcd" role="button">Get the extension here</a></p>
              </div>
          </div>
      </div>
      <div ng-if="enoughGroups === false" class="jumbotron">
          <div class="container">
              <div class="feed-container">
                  <h1>Not enough data</h1>
                  <p>You have not gathered enough data to get any results.</p>
                  <p>It might take several days to gather enough data, be patient.</p>
              </div>
          </div>
      </div>
  </body>
</html>
