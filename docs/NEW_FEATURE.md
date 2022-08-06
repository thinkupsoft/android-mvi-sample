## Make a feature

To learn how to start with this MVI ThinkUp template, we will make a feature 1.
Clone the [smaple project](https://gitlab.com/thinkupsoft/android/mvi-compose-sample)
and follow it step by step.

## Feature 1:

Consume a service from https://openweathermap.org/ to display in the UI
and persist in the local database.
If the database has data, show it while the service ends and update the UI.

### First step: configure the service

#### Get a refresh auth-token service

Go to 'RefreshService' class and change the refresh method url for a real and modify your RefreshResponse.

** in this case openweathermap dont use a refresh service.

#### Add a service interface

Add a interface class called 'WeatherService'.
This class need parse the response into a model.
Create a 'WeatherResponse' class, parsing the data as you need.
Add a factory to inject it into a repository:
- Goto DiWrapper and add, on moduleServices, a WeatherService factory. (remember: openweathermap don't has a refresh service)

Now you have a service, but how do you consume it?

### Second step: add a repository

Create a 'WeatherRepository' class extending from 'BaseRepository' with a parameter 'WeatherService' type.
Add a suspend function 'getCurrent'.
Add a factory to inject it into a usecase:
- Goto DiWrapper and add, on moduleRepositories, a WeatherRepository factory.


### Third step: add a usecase

Under your feature package ('home' to this sample), create a 'WeatherServiceUseCase' class extending from UseCase,
with a parameter 'WeatherRepository' type.
Add a factory to inject it into a usecase:
- Go to DiWrapper and add, on moduleUseCase, a WeatherServiceUseCase factory.

### Fourth step: add a viewmodel

Under your feature package, create a 'WeatherViewModel' class extending from BaseViewModel,
with a parameter 'WeatherServiceUseCase' type.
Add a LiveData method to execute the usecase involved in an executeService function.
Add a factory to inject it into a viewmodels:
- Goto DiWrapper and add, on moduleViewModels, a WeatherViewModel factory.

### Fifth step: display data

On your activity (extending from BaseActivity) or Fragment (extending from BaseFragment) inject a 'WeatherViewModel',
using createViewModel extension.
Call the function on the viewModel to get service weather data and display it.

### Sixth step: storage on local DB

Like a service, but in storage module, create an interface room Dao extended from IDao.
Declare an abstract function on your appDatabase class to get an instance of your Dao.
Initialize it from DiWrapper, and add it to your Repository.
Save and get data from your Database, and create an usecase.
In your viewModel get data, from local database first, and before from the cloud.