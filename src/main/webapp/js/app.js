/**
 * Created by JanTore on 16.10.2014.
 */
angular.module('app', ['ngSanitize'])
    .factory('Backend', ['$http', function($http) {
        function getNews(id) {
            var request = $http({
                method: 'GET',
                url: 'resources/articles/'+encodeURI(id)
            }).then(function(response) {
                return response;
            });
            return request;
        }
        function getGroups(username) {
            var request = $http({
                method: 'GET',
                url: '/resources/groups/'+encodeURI(username)
            }).then(function(response) {
                return response;
            });
            return request;
        }
        return({
            getNews: getNews,
            getGroups: getGroups
        });
    }])
    .controller('AppCtrl', ['$scope', '$sce', 'Backend',
        function($scope, $sce, Backend) {
            $scope.news = [];
            $scope.interests = [];
            $scope.interestGroups = [];
            $scope.username = '';
            $scope.enoughGroups = true;

            $scope.getRecommendations = function(id) {
                var newsPromise = Backend.getNews(id);
                newsPromise.then(function(response){
                    $scope.news = response.data.articles;
                    $scope.responseInfo = {};
                    $scope.responseInfo.time = response.data.responseTime;
                    $scope.responseInfo.hitsTotal = response.data.totalHits;
                    $scope.responseInfo.hitsShowing = response.data.hitsShowing;
                    $scope.responseInfo.groupId = id;
                });
            };

            document.addEventListener("feedRecorderInterestsEvent", function(ev) {
                $scope.username = ev.detail;

                var groupsPromise = Backend.getGroups($scope.username);
                groupsPromise.then(function(response) {
                    $scope.interestGroups = response.data;
                    if ($scope.interestGroups < 1) {
                        $scope.enoughGroups = false;
                    } else {
                        $scope.getRecommendations($scope.interestGroups[0].groupId)
                    }
                });
            });

            $scope.createTrustedHtml = function(html) {
                return $sce.trustAsHtml(html);
            };

            $scope.getSource = function(url) {
                var rem = url.replace(/.*?:\/\//g, "");
                var splits = rem.split('/');
                return splits[0].replace('www.', '');
            };

            $scope.parseDate = function(date) {
                return date.replace("T", " ").replace("Z", "");
            };
    }]);