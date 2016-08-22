(function() {

    'use strict';

    var app = angular.module("StockMonitor", []);
    // app.config(function($routeProvider) {
    //     $routeProvider
    //         .when("/SMP", {
    //             templateUrl: "index.html",
    //             controller: 'myController'
    //         })
    //         .when("/login.html", {
    //             templateUrl: "login.html",
    //             controller: 'myController'
    //         });
    // });

    app.controller("myController", ['$scope', '$http', '$location', '$q', '$log', function($scope, $http, $location, $q, $log) {

        var vm = this;
        $scope.stockArr = [];
        $scope.newSymbol = '';
        $scope.oneStock = {};
        $scope.addStock = addStock;
        $scope.deleteStock = deleteStock;
        $scope.updateChart = updateChart;
        $scope.userLogin = userLogin;
        $scope.userLogout = userLogout;
        $scope.alert = false;
        $scope.marketClose = false;
        $scope.predicate = 'id';
        $scope.repeatHandle;
        $scope.interval = 5000;
        $scope.stockSymbol;
        $scope.userInfo;
        // $scope.userInfo.email ;
        // $scope.userInfo.password ;
        var visualization;



        $scope.history_price = [];

        init();

        // $scope.order = function(x) {
        //     $scope.myOrderBy = x;
        // }
        // $scope.go = function(path) {
        //     $location.path(path);
        // };

        //set update frequency
        // function init() {
        //     setInterval(updatePrice, 5000);
        // }
        function init() {
            // console.log("initing....");
            $scope.repeatHandle = setInterval(updatePrice, 5000);
            setNextTimer();
        }

        function setNextTimer() {
            console.log("setNextTimer");
            var timeoutSec = checkTimeout();

            if (timeoutSec > 0) { // currently it's close
                $scope.marketClose = true;
                clearInterval($scope.repeatHandle);
                setTimeout(function() { startUpdate(); }, timeoutSec);
                // console.log("Market is closed!");
            } else { // it's open
                $scope.marketClose = false;
                setTimeout(function() { pauseUpdate(); }, -timeoutSec);
                // console.log("Market is open!");
            }
        }

        function startUpdate() {
            console.log("startUpdate");
            $scope.repeatHandle = setInterval(updatePrice, 5000);
            // console.log("startUpdate setNextTimer");
            setNextTimer();
        }
        // function changecolors() {
        //     x = 1;
        //     setInterval(change, 5000);
        // }

        // function change() {
        //     if (x === 1) {
        //         color = "red";
        //         x = 2;
        //     } else {
        //         color = "green";
        //         x = 1;
        //     }

        //     document.body.style.background = color;
        // }

        function pauseUpdate() {
            clearInterval($scope.repeatHandle);
            setNextTimer();
        }
        //
        //
        function checkTimeout() {
            var d = new Date();
            var n = d.getDay();
            var h = d.getHours();
            var m = d.getMinutes();
            var s = d.getSeconds();
            if (n == 6 || n == 7) // sat sun, close
                return (((8 - n) * 24 + 6) * 60 + 30) * 60000 - ((h * 60 + m) * 60 + s) * 1000;

            else if (h >= 13 || h < 6 || (h == 6 && m <= 29)) { // close
                if (n == 5)
                    return (((8 - n) * 24 + 6) * 60 + 30) * 60000 - ((h * 60 + m) * 60 + s) * 1000;
                else
                    return ((24 + 6) * 60 + 30) * 60000 - ((h * 60 + m) * 60 + s) * 1000;
                // console.log("Market is closed!");
            } else { // return a negative value, means market is open.
                return ((h * 60 + m) * 60 + s) * 1000 - ((13) * 60) * 60000;

            }
        }



        // update stock price in stock table page
        function updatePrice() {
            angular.forEach($scope.stockArr, function(item, index) {
                // console.log("updating price");
                getPriceBySymbol(item.symbol).then(function() {
                    item.high = $scope.oneStock.high;
                    item.low = $scope.oneStock.low;
                    item.open = $scope.oneStock.open;
                    item.close = $scope.oneStock.close;
                    item.price = $scope.oneStock.price;
                    item.volume = $scope.oneStock.volume;
                });

            });
        }


        // get stock price by stock's symbol froms server
        function getPriceBySymbol(stockSymbol) {
            var deferred = $q.defer();

            if (stockSymbol) {
                $http.get('YahooRealtimeData', { responseType: 'json', params: { symbol: stockSymbol } }).then(
                    function(res) {
                        angular.merge($scope.oneStock, res.data);
                        // console.log("update price to front");
                        // console.log($scope.stockArr);
                        deferred.resolve();
                    },
                    function(err) {
                        console.log("Fetch price failure");
                        deferred.reject();
                    }
                );
            }
            return deferred.promise;
        }


        // get stock info by stock's symbol from server
        function getInfoBySymbol(stockSymbol) {
            var deferred = $q.defer();
            if (stockSymbol) {
                $http.get('getOneInfo', { responseType: 'json', params: { symbol: stockSymbol } }).then(
                    function(res) {
                        angular.merge($scope.oneStock, res.data);
                        deferred.resolve();
                    },
                    function(err) {
                        deferred.reject();
                    }
                );

            }
            return deferred.promise;

        }



        // add new stock entry in database
        function addStockDB(stockSymbol) {

            var deferred = $q.defer();
            $http.get('addOneStockToService', { params: { symbol: stockSymbol } }).then(
                function(res) {
                    deferred.resolve();
                },
                function(err) {
                    console.log("add company failure");
                    deferred.reject();
                }
            );

            return deferred.promise;

        }


        //check if the stock already subscribed 
        function objectWithPropExists(array1, propName, propVal) {
            for (var i = 0, k = array1.length; i < k; i++) {
                if (array1[i][propName] === propVal) return true;
            }
            return false;
        }


        // add new stock in stock table page
        function addStock(stockSymbol) {

            if (!(objectWithPropExists($scope.stockArr, 'symbol', stockSymbol))) {

                $scope.alert = false;
                addStockDB(stockSymbol).then(function() {

                    getInfoBySymbol(stockSymbol).then(function() {
                        getPriceBySymbol(stockSymbol).then(function() {
                            $scope.stockArr.push($scope.oneStock);
                            $scope.oneStock = {};
                            $scope.getPeriodCompanyStockInfo('A_Week_' + stockSymbol);
                            $scope.stockSymbol = '';
                        });
                    });
                });


            } else {
                $scope.alert = true;
            }
        }

        // $scope.clearInput = function() {
        //     $scope.stockSymbol = null;
        // }

        //remove one stock from page
        function removeByKey(array, params) {
            array.some(function(item, index) {
                if (array[index][params.key] === params.value) {
                    // found it!
                    array.splice(index, 1);
                    return true; // stops the loop
                }
                return false;
            });
            return array;
        }

        //delete one stock form db
        function deleteStock(stockSymbol) {
            removeByKey($scope.stockArr, {
                key: 'symbol',
                value: stockSymbol
            });
            $http.get('delOneStock', { params: { symbol: stockSymbol } }).then(
                function(res) {
                    console.log("delete stock from db success!");
                },
                function(err) {
                    console.log("Returned info error.");
                }
            );
        }


        $scope.getPeriodCompanyStockInfo = function(symbolTimeRange) {
            $http.get('GetOneHistoryPrice', { responseType: 'json', params: { symbol: symbolTimeRange } }).then(
                function(res) {
                    $scope.history_price = res.data;
                    $scope.history_price.sort(function(a, b) {
                        return new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime();
                    });
                    angular.forEach($scope.history_price, function(item, index) {
                        item.index = index.toString();
                    });
                    $scope.updateChart();
                },
                function(err) {
                    $scope.history_price = [];
                    console.log("Returned info error.");
                }
            );
        };

        //???????

        //delete one stock form db
        function userLogin() {
            console.log("initing login....");
            // var data = $.param({
            //     username: $scope.userInfo.email,
            //     password: $scope.userInfo.password
            // });
            $http.get('UserLoginInfo', { responseType: 'json', 
                params:{"username": $scope.userInfo.email, "password": $scope.userInfo.password}})
            .then(
                function(res) {
                    $('#needlogin').css({ 'display': 'none' });
                    $('#loginform').css({ 'display': 'none' });
                    $(".welcome").css({ 'display': 'block' });
                    $('#logined').css({ 'display': 'block' });
                    // $(location).attr('href', 'index.html')

                    console.log("login success!");
                },
                function(err) {
                    $(".login").css({ 'display': 'block' });
                    console.log("login error.");
                }
            );
        }
               

        function userLogout() {
            $http.get('userLogout', { responseType: 'json', params: { user: $scope.userInfo.email } }).then(
                function(res) {
                    $(".welcome").css({ 'display': 'block' });
                    console.log("login success!");
                },
                function(err) {
                    $(".login").css({ 'display': 'block' });
                    console.log("login error.");
                }
            );
        }


        // $(document).mouseup(function(e) {
        //     var container = $(".wrap");

        //     if (!container.is(e.target) // if the target of the click isn't the container...
        //         && container.has(e.target).length === 0) // ... nor a descendant of the container
        //     {
        //         // container.hide();
        //         $('#navthing').hide();
        //         $('#loginform').removeClass('green');
        //     }
        // });

        // $(document).mouseup(function(e) {
        //     var container = $(".wrap");

        //     if (!container.is(e.target) // if the target of the click isn't the container...
        //         && container.has(e.target).length === 0) // ... nor a descendant of the container
        //     {
        //         // container.hide();
        //         $('#navthing').hide();
        //         $('#loginform').removeClass('green');
        //     }
        // });
        // $('input[type="submit"]').mousedown(function() {
        //     $(this).css('background', '#2ecc71');
        // });
        // $('input[type="submit"]').mouseup(function() {
        //     $(this).css('background', '#1abc9c');
        // });

        // $('#loginform').click(function() {
        //     $('.login').fadeToggle('slow');
        //     $(this).toggleClass('green');
        // });

        function updateChart() {
            if (typeof visualization !== "undefined") {
                var svg = document.getElementById("StockView");
                var svgParent = svg.parentNode;
                svgParent.removeChild(svg);
                svg = document.createElement("StockView"); // Create a <li> node
                svg.setAttribute("id", "StockView");
                svgParent.appendChild(svg);
            }


            visualization = d3plus.viz()
                .container("#StockView")
                .data($scope.history_price)
                .type("bar")
                .margin("10px 20px")
                .messages("Loading Data...")
                .id(["index", "symbol"]) // key for which our data is unique on
                .y("high") // key to use for y-axis
                .x({
                    value: "timestamp",
                    label: "Time"
                })
                .time("timestamp") //{ "format": "%Y-%m-%d", "value": "timestamp" }
                .tooltip(["high", "low", "open", "close", "timestamp", "price"])
                .ui([{
                    label: "Visualization Type",
                    method: "type",
                    value: ["bar", "line"]
                }])
                .height(400)
                .font({ "family": "Times" })
                .format({
                    "text": function(text, params) {
                        if (text === "price") {
                            return "price";
                        } else {
                            return d3plus.string.title(text, params);
                        }

                    },
                    "number": function(number, params) {
                        var formatted = d3plus.number.format(number, params);
                        if (params.key === "price" || params.key === "high" || params.key === "low" || params.key === "open" || params.key === "close") {
                            return "$" + formatted + " USD";
                        } else {
                            return formatted;
                        }

                    }
                })
                .title("Logic Stock Monitor")
                .title({
                    "sub": "Realtime Stock Monitor System"
                })
                .footer({
                    "link": "https://www.linkedin.com/in/will-zhang-b5194411b?trk=hp-identity-name",
                    "value": "Click here to find me"
                })
                .draw();
        }

    }]);

})();
