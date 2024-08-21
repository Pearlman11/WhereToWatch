# Deadline


## Part 1.1: App Description

> Description of app: The app allows users to search for a movie or television show,
> based on their search, they will be taken to an information tab, where OMDBAPI provides
> the Title, Release year, Plot and actors. The app then uses the String title from the omdb response, converts it to a
> watchModeID via a method which returns the key from the title value pairs provided by https://api.watchmode.com/. Using this response
> the app then stores the similar titles (displayed in the information tab) and populates a TableView<Sources> which provides the user
> with information regarding where to watch the movie/show, the cost (if available to rent) or is blank if it requires a subscription.
> If the movie or show requires a subscription it is present in the "Type" column, for reference. Furthermore the app allows the user to
> copy the web url for the streaming service (provided by https://api.watchmode.com/) for their use on their personal device. NOTE: while
> there is no harm in using "CTL+C" or "CMD+C" to copy, the app allows for click to copy, and stores the url on the clipboard of the user.

> Primary Functions available to user: Make requests for streaming services based on the title of a movie or tv show, if there are multiple
> titles with the same name, the user can provide the release year to further filter the results. The app also provides the user a list of
> similar titles, based on the titles the user could then search for a new movie or show they have not watched based on the recommendations.

> Description of API's used and how they are connected: The app utalizes the information from the OMDB response to get the WatchModeID which
> is required to make a call to the watchMode API. Furthermore the OMDBapi would not be suffiecent to provide all of the information for the
> users request. The use of both apis, allows the user to get all of the relevant information about the movie or show they are searching for.

> GitHub url: https://github.com/Pearlman11/cs1302-api-app

## Part 1.2: APIs

### API 1: OMDbapi

```
"https://www.omdbapi.com/?&apikey=_____&t=Fight+Club&type=movie&r=json&y=1999"
```

> Rate Limit: 1,000 free requests daily.
> API Key: Free (required)


### API 2: WatchModeAPI

```
"https://api.watchmode.com/v1/title/details/?apiKey=_____&append_to_response=sources"
```

> Rate Limit: 1,000 free requests monthly
> API Key: Free (required)

## Part 2:  Somethign new and/or exciting I learned from working on this project:
> While we were aware of how to use API's prior to this project, our professors provided us with a great deal of assistance,
> examples of calls, how to handle responses and walked us through the documentation for the api. This project required us to read and
> understand the documentation, generate and use API Keys and think creatively of an idea for how to utalize our knowledge from this course.
> This project was the first project I personally have built from "scratch" (despite alot of starter code from our professors)
> and once again showed me why I decided on this major.

## Part 3: Retrospect
> If I could start this project over from scratch, I would make sure to use git branching and commits more than I did.
> there were multiple instances where I forgot to create a new branch to start working on and because I made a logical error
> in my code I was forced to manually debug and in some cases delete blocks of code to get the functionality of the app back to where
> it was prior. Had I branched, added and commited more, I would have been able to recover much more elegantly than I did.
> Furthermore I would take considerably less time to find the API's that I wanted to use. I spent hours and hours reading to think of a new,
> cool idea rather than put my learning to work and appreciate the fact that if an idea comes to my head in the future, I actually have the
> ability to create.
