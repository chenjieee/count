var countApp = angular.module('countApp');

countApp.controller('countController', function($scope, $http, growl, $modal) {

    $scope.types = [ 'First Letter', 'Whole Word' ];

    $scope.config = {
        name : 'sample',
        type : $scope.types[0]
    };

    $scope.submit = function() {
        $http.post('api/execute', {
            name : $scope.name,
            type : $scope.type
        }).success(function(data) {
            $scope.results = data || [];
            $scope.draw();
        });
    };

    $scope.draw = function() {
        d3.select('#chart_holder').html('');

        var margin = { top: 20, right: 20, bottom: 30, left: 40 };
        var width = 900 - margin.left - margin.right;
        var height = 500 - margin.top - margin.bottom;

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
