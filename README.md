# Virtual-Cafe
Simulating a Cafe experience using a Client-Server Architecture and Socket Programming.


## Objective


Each client application will play the role of a Customer who wants to order tea or coffee. The server application will play the role of the café's virtual Barista, who processes the orders, prepares tea and coffee, and delivers orders back to the customers.

### Features of Customer

- Placing Orders: Customer can type commands in the terminal to order tea or coffee, specifying the quantity and type of beverage. See commands.txt

- Order Status Inquiry: Customer can inquire about the status of their order at any time. See commands.txt

- Leaving the Cafe: Customer can leave the cafe at any time. See commands.txt


### Features of Barista
- Barista Processes and Responds to Customer Orders.
- Barista returns status of an order.
- Barista handles inappropraite Customer Requests.



### Usage and Installation

To run the Virtual Cafe application locally, follow these steps:

- Clone this Repository.
- Run Barista using the Below:
```bash 
 javac -cp "." Barista.java
```
- Run Customer using the Below:
```bash 
 javac -cp "." Customer.java
```
## Improvements

- Extend Identification of Customers using an ID

- Barista contains a Brewing Area, Tray Area and Waiting Area. Alternative design decisions are welcome.

- Add functionality to Add to and Order instead of creating a new Order.