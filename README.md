# Fligh_app

![maxresdefault](https://user-images.githubusercontent.com/42437810/64408710-0a26b180-d0a5-11e9-81a8-daa49a4c64ee.jpg)

In the Airline Tickets example, we need to make multiple dependent HTTP calls to render the screen. At first, all the tickets will be fetched by making single HTTP call excluding the price and available seats on each airline. The realtime price and seats availability will be fetched separately for each ticket.

Let’s say we have 20 tickets available, all the 20 tickets will be fetched in first HTTP call. Next, 20 subsequent HTTP calls will be made parallelly to fetch the price and seats information.
Using Rxjava,Retrofit,okhttpclient,spinkit,glide,butterknife.
