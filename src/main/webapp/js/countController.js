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
		
		var margin = {top: 20, right: 20, bottom: 30, left: 40},
	    width = 900 - margin.left - margin.right,
	    height = 500 - margin.top - margin.bottom;

		var x = d3.scale.ordinal()
		    .rangeRoundBands([0, width], .1);
	
		var y = d3.scale.linear()
		    .range([height, 0]);
	
		var xAxis = d3.svg.axis()
		    .scale(x)
		    .orient('bottom');
	
		var yAxis = d3.svg.axis()
		    .scale(y)
		    .orient('left')
		    .ticks(10, '%');
	
		var svg = d3.select('#chart_holder').append('svg')
		    .attr('width', width + margin.left + margin.right)
		    .attr('height', height + margin.top + margin.bottom)
		  .append('g')
		    .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
	
		d3.tsv('data.tsv', type, function(error, data) {
		  if (error) throw error;
		  
		  data = [
		          { letter : 'a', frequency : 0.3 },
		          { letter : 'b', frequency : 0.4 },
		          { letter : 'c', frequency : 0.2 },
		          { letter : 'd', frequency : 0.1 },
		          { letter : 'e', frequency : 0.7 }
		  ];
		  console.log('----  got data...');
		  
		  x.domain(data.map(function(d) { return d.letter; }));
		  y.domain([0, d3.max(data, function(d) { return d.frequency; })]);
	
		  svg.append('g')
		      .attr('class', 'x axis')
		      .attr('transform', 'translate(0,' + height + ')')
		      .call(xAxis);
	
		  svg.append('g')
		      .attr('class', 'y axis')
		      .call(yAxis)
		    .append('text')
		      .attr('transform', 'rotate(-90)')
		      .attr('y', 6)
		      .attr('dy', '.71em')
		      .style('text-anchor', 'end')
		      .text('Frequency');
	
		  svg.selectAll('.bar')
		      .data(data)
		    .enter().append('rect')
		      .attr('class', 'bar')
		      .attr('x', function(d) { return x(d.letter); })
		      .attr('width', x.rangeBand())
		      .attr('y', function(d) { return y(d.frequency); })
		      .attr('height', function(d) { return height - y(d.frequency); });
		});
	
		function type(d) {
		  d.frequency = +d.frequency;
		  return d;
		}
	};
	
});
