let cart = $("#cart");

/**
 * Handle the data returned by IndexServlet
 * @param resultDataString jsonObject, consists of session info
 */
function handleSessionData(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle session response");
    console.log(resultDataJson);
    console.log(resultDataJson["sessionID"]);

    // show the session information
    $("#sessionID").text("Session ID: " + resultDataJson["sessionID"]);
    $("#lastAccessTime").text("Last access time: " + resultDataJson["lastAccessTime"]);

    // show cart information
    handleCartArray(resultDataJson["previousItems"]);
}

/**
 * Handle the items in item list
 * @param resultArray jsonObject, needs to be parsed to html
 */
function handleCartArray(resultData) {
    let cart_table = jQuery("#cart_table_body");
    // change it to html list
    console.log("gets here");

    for (let i = 0; i < resultData.length; i++) {
        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-movie.html with id passed with GET url parameter
            resultData[i]["movie_title"] +     // display star_name for the link text
            "</th>";

        rowHTML += "<th>" + resultData[i]["movie_price"] + "</th>";
        rowHTML += "<th>" + resultData[i]["quantity"] + "</th>";

        rowHTML += "<th>$" + resultData[i]["quantity"] * resultData[i]["movie_price"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        cart_table.append(rowHTML);
    }
}

/**
 * Submit form content with POST method
 * @param cartEvent
 */
function handleCartInfo(cartEvent) {
    console.log("submit cart form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    cartEvent.preventDefault();

    $.ajax("api/cart", {
        method: "GET",
        data: cart.serialize(),
        success: resultData=> handleCartArray(resultData)
        }
    );

    // clear input form
    cart[0].reset();
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET",
    url: "api/cart",
    success: (resultData) => handleCartArray(resultData)
});


