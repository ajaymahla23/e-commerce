(function($) {
	'use strict';
	$(function() {
		Chart.defaults.global.legend.labels.usePointStyle = true;

		if ($("#visit-sale-chart").length) {
			Chart.defaults.global.legend.labels.usePointStyle = true;
			var ctx = document.getElementById('visit-sale-chart').getContext("2d");

			var gradientStrokeViolet = ctx.createLinearGradient(0, 0, 0, 181);
			gradientStrokeViolet.addColorStop(0, 'rgba(218, 140, 255, 1)');
			gradientStrokeViolet.addColorStop(1, 'rgba(154, 85, 255, 1)');
			var gradientLegendViolet = 'linear-gradient(to right, rgba(218, 140, 255, 1), rgba(154, 85, 255, 1))';

			var gradientStrokeBlue = ctx.createLinearGradient(0, 0, 0, 360);
			gradientStrokeBlue.addColorStop(0, 'rgba(54, 215, 232, 1)');
			gradientStrokeBlue.addColorStop(1, 'rgba(177, 148, 250, 1)');
			var gradientLegendBlue = 'linear-gradient(to right, rgba(54, 215, 232, 1), rgba(177, 148, 250, 1))';

			var gradientStrokeRed = ctx.createLinearGradient(0, 0, 0, 300);
			gradientStrokeRed.addColorStop(0, 'rgba(255, 191, 150, 1)');
			gradientStrokeRed.addColorStop(1, 'rgba(254, 112, 150, 1)');
			var gradientLegendRed = 'linear-gradient(to right, rgba(255, 191, 150, 1), rgba(254, 112, 150, 1))';

			var myChart = new Chart(ctx, {
				type: 'bar',
				data: {
					labels: ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG'],
					datasets: [
						{
							label: "CHN",
							borderColor: gradientStrokeViolet,
							backgroundColor: gradientStrokeViolet,
							hoverBackgroundColor: gradientStrokeViolet,
							legendColor: gradientLegendViolet,
							pointRadius: 0,
							fill: false,
							borderWidth: 1,
							fill: 'origin',
							data: [20, 40, 15, 35, 25, 50, 30, 20]
						},
						{
							label: "USA",
							borderColor: gradientStrokeRed,
							backgroundColor: gradientStrokeRed,
							hoverBackgroundColor: gradientStrokeRed,
							legendColor: gradientLegendRed,
							pointRadius: 0,
							fill: false,
							borderWidth: 1,
							fill: 'origin',
							data: [40, 30, 20, 10, 50, 15, 35, 40]
						},
						{
							label: "UK",
							borderColor: gradientStrokeBlue,
							backgroundColor: gradientStrokeBlue,
							hoverBackgroundColor: gradientStrokeBlue,
							legendColor: gradientLegendBlue,
							pointRadius: 0,
							fill: false,
							borderWidth: 1,
							fill: 'origin',
							data: [70, 10, 30, 40, 25, 50, 15, 30]
						}
					]
				},
				options: {
					responsive: true,
					legend: false,
					legendCallback: function(chart) {
						var text = [];
						text.push('<ul>');
						for (var i = 0; i < chart.data.datasets.length; i++) {
							text.push('<li><span class="legend-dots" style="background:' +
								chart.data.datasets[i].legendColor +
								'"></span>');
							if (chart.data.datasets[i].label) {
								text.push(chart.data.datasets[i].label);
							}
							text.push('</li>');
						}
						text.push('</ul>');
						return text.join('');
					},
					scales: {
						yAxes: [{
							ticks: {
								display: false,
								min: 0,
								stepSize: 20,
								max: 80
							},
							gridLines: {
								drawBorder: false,
								color: 'rgba(235,237,242,1)',
								zeroLineColor: 'rgba(235,237,242,1)'
							}
						}],
						xAxes: [{
							gridLines: {
								display: false,
								drawBorder: false,
								color: 'rgba(0,0,0,1)',
								zeroLineColor: 'rgba(235,237,242,1)'
							},
							ticks: {
								padding: 20,
								fontColor: "#9c9fa6",
								autoSkip: true,
							},
							categoryPercentage: 0.5,
							barPercentage: 0.5
						}]
					}
				},
				elements: {
					point: {
						radius: 0
					}
				}
			})
			$("#visit-sale-chart-legend").html(myChart.generateLegend());
		}

		//=====pie chart

		var countByuser = JSON.parse(document.getElementById("countByuser").value);
		var countByActiveUser = JSON.parse(document.getElementById("countByActiveUser").value);
		var listofverifyStore = JSON.parse(document.getElementById("listofverifyStore").value);

		var doughnutPieData = {
			datasets: [{
				data: [countByuser, countByActiveUser, listofverifyStore],
				backgroundColor: [
					'rgba(255, 99, 132, 0.5)',
					'rgba(54, 162, 235, 0.5)',
					'rgba(255, 206, 86, 0.5)',
					'rgba(75, 192, 192, 0.5)',
					'rgba(153, 102, 255, 0.5)',
					'rgba(255, 159, 64, 0.5)'
				],
				borderColor: [
					'rgba(255,99,132,1)',
					'rgba(54, 162, 235, 1)',
					'rgba(255, 206, 86, 1)',
					'rgba(75, 192, 192, 1)',
					'rgba(153, 102, 255, 1)',
					'rgba(255, 159, 64, 1)'
				],
			}],

			// These labels appear in the legend and in the tooltips when hovering different arcs
			labels: [
				'Total Users',
				'Active Users',
				'Active Stores',
			]
		};


		var doughnutPieOptions = {
			responsive: true,
			animation: {
				animateScale: true,
				animateRotate: true
			}
		};
		if ($("#pieChart").length) {
			var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
			var pieChart = new Chart(pieChartCanvas, {
				type: 'pie',
				data: doughnutPieData,
				options: {
					responsive: true,


					title: {
						display: false,
						text: "Chart.js - Bar Chart with Rounded Tops (drawRoundedTopRectangle Method)"
					},
					legend: {
						display: true,
						position: 'left'
					},
				}
			});
		}

		//============================================================================================================traffic-chart
		if ($("#traffic-chart").length) {
			var sumOfTotalIncome = JSON.parse(document.getElementById("sumOfTotalIncome").value);
			var sumOfAmtLastOneMonth = JSON.parse(document.getElementById("sumOfAmtLastOneMonth").value);
			var sumOfAmtLastOneWeek = JSON.parse(document.getElementById("sumOfAmtLastOneWeek").value);
			var gradientStrokeBlue = ctx.createLinearGradient(0, 0, 0, 181);
			gradientStrokeBlue.addColorStop(0, 'rgba(54, 215, 232, 1)');
			gradientStrokeBlue.addColorStop(1, 'rgba(177, 148, 250, 1)');
			var gradientLegendBlue = 'linear-gradient(to right, rgba(54, 215, 232, 1), rgba(177, 148, 250, 1))';

			var gradientStrokeRed = ctx.createLinearGradient(0, 0, 0, 50);
			gradientStrokeRed.addColorStop(0, 'rgba(255, 191, 150, 1)');
			gradientStrokeRed.addColorStop(1, 'rgba(254, 112, 150, 1)');
			var gradientLegendRed = 'linear-gradient(to right, rgba(255, 191, 150, 1), rgba(254, 112, 150, 1))';

			var gradientStrokeGreen = ctx.createLinearGradient(0, 0, 0, 300);
			gradientStrokeGreen.addColorStop(0, 'rgba(6, 185, 157, 1)');
			gradientStrokeGreen.addColorStop(1, 'rgba(132, 217, 210, 1)');
			var gradientLegendGreen = 'linear-gradient(to right, rgba(6, 185, 157, 1), rgba(132, 217, 210, 1))';

			var trafficChartData = {
				datasets: [{
					data: [sumOfTotalIncome, sumOfAmtLastOneMonth, sumOfAmtLastOneWeek],
					backgroundColor: [
						gradientStrokeBlue,
						gradientStrokeGreen,
						gradientStrokeRed
					],
					hoverBackgroundColor: [
						gradientStrokeBlue,
						gradientStrokeGreen,
						gradientStrokeRed
					],
					borderColor: [
						gradientStrokeBlue,
						gradientStrokeGreen,
						gradientStrokeRed
					],
					legendColor: [
						gradientLegendBlue,
						gradientLegendGreen,
						gradientLegendRed
					]
				}],

				// These labels appear in the legend and in the tooltips when hovering different arcs
				labels: [
					'Total Income =',
					'Last 30 days =',
					'Last 7 days =',
				]
			};
			var trafficChartOptions = {
				responsive: true,
				animation: {
					animateScale: true,
					animateRotate: true
				},
				legend: false,
				legendCallback: function(chart) {
					var text = [];
					text.push('<ul>');
					for (var i = 0; i < trafficChartData.datasets[0].data.length; i++) {
						text.push('<li><span class="legend-dots" style="background:' +
							trafficChartData.datasets[0].legendColor[i] +
							'"></span>');
						if (trafficChartData.labels[i]) {
							text.push(trafficChartData.labels[i]);
						}
						text.push('<span class="float-right">' + ' $' + trafficChartData.datasets[0].data[i] + '</span>')
						text.push('</li>');
					}
					text.push('</ul>');
					return text.join('');
				}
			};
			var trafficChartCanvas = $("#traffic-chart").get(0).getContext("2d");
			var trafficChart = new Chart(trafficChartCanvas, {
				type: 'doughnut',
				data: trafficChartData,
				options: trafficChartOptions
			});
			$("#traffic-chart-legend").html(trafficChart.generateLegend());
		}
		if ($("#inline-datepicker").length) {
			$('#inline-datepicker').datepicker({
				enableOnReadonly: true,
				todayHighlight: true,
			});
		}
	});
})(jQuery);
