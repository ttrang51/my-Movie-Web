/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleMovieResult(resultData) {
    console.log("handleStarResult: populating movies table from resultData");

    // Populate the movies table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-movie.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_name"] +     // display star_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_dir"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_gen"] + "</th>";

        stars_array = resultData[i]["movie_star"].split(","); // Length 3
        stars_ids_array = resultData[i]["movie_star_ids"].split(","); // Length 3

        rowHTML +=
            "<th>" +
            '<a href="single-star.html?id=' + stars_ids_array[0] + '">'
            + stars_array[0] +     // display star_name for the link text
            '</a>, ' +
            '<a href="single-star.html?id=' + stars_ids_array[1] + '">'
            + stars_array[1] +     // display star_name for the link text
            '</a>, ' +
            '<a href="single-star.html?id=' + stars_ids_array[2] + '">'
            + stars_array[2] +     // display star_name for the link text
            '</a>, '
            + "</th>";


        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
// Get parameters from URL
let title = getParameterByName('movie_title');
let year = getParameterByName('year');
let director_name = getParameterByName('director_name');
let star_name = getParameterByName('star_name');

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movies-search?movie_title=" + title + "&director_name=" + director_name + "&year=" + year + "&star_name=" + star_name, // Setting request url, which is mapped by MoviesServlet in Movies.java
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});