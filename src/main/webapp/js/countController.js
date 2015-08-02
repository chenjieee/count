var countApp = angular.module('countApp');

countApp.controller('countController', function($scope, $http, growl) {

    $scope.inputPaths = [ 'input-00001', 'input-00002', 'input-00003' ];

    $scope.config = {
        inputPath : $scope.inputPaths[0],
        minLength : 0,
        maxLength : 255,
        caseInsensitive : true
    };

    $scope.executing = false;

    $scope.execute = function() {
        $scope.executing = true;
        growl.success('The job has been submitted.');

        var url = 'api/execute?inputPath=' + $scope.config.inputPath;
        url = url + '&caseInsensitive=' + $scope.config.caseInsensitive;
        if ($scope.config.minLength) {
            url = url + '&minLength=' + $scope.config.minLength;
        }
        if ($scope.config.maxLength) {
            url = url + '&maxLength=' + $scope.config.maxLength;
        }
        $http.get(url).success(function(data) {
            $scope.results = data || [];

            var index = 1;
            $scope.results.forEach(function(result) {
                result.index = index++;
            });

            $scope.draw();

            $scope.executing = false;
            growl.success('The job has completed successfully.');
        });
    };

    $scope.draw = function() {
        d3.select('#chart_holder').html('');

        var margin = { top: 20, right: 20, bottom: 30, left: 50 };
        var width = 800 - margin.left - margin.right;
        var height = 400 - margin.top - margin.bottom;

        var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);

        var y = d3.scale.linear()
            .range([height, 0]);

        var xAxis = d3.svg.axis()
            .scale(x)
            .orient('bottom');

        var yAxis = d3.svg.axis()
            .scale(y)
            .orient('left');

        var svg = d3.select('#chart_holder').append('svg')
            .attr('width', width + margin.left + margin.right)
            .attr('height', height + margin.top + margin.bottom)
          .append('g')
            .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

        x.domain($scope.results.map(function(d) { return d.word; }));
        y.domain([0, d3.max($scope.results, function(d) { return d.count; })]);

        svg.append('g')
            .attr('class', 'x axis')
            .attr('transform', 'translate(0,' + height + ')')
            .call(xAxis);

        svg.append('g')
            .attr('class', 'y axis')
            .call(yAxis)
          .append('text')
            .attr('transform', 'rotate(-90)')
            .attr('y', 3)
            .attr('dy', '.5em')
            .style('text-anchor', 'end')
            .text('Count');

        svg.selectAll('.bar')
            .data($scope.results)
          .enter().append('rect')
            .attr('class', 'bar')
            .attr('x', function(d) { return x(d.word); })
            .attr('width', x.rangeBand())
            .attr('y', function(d) { return y(d.count); })
            .attr('height', function(d) { return height - y(d.count); });
    };

});
