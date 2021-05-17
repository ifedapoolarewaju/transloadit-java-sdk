### 0.2.0 / 2021-05-17 ###
* Added retry functionality for assemblies in case of reaching the rate limit

### 0.1.6 / 2021-02-24 ###

* Fix bug that doesn't allow usage of templates that have disabled allow steps override. 
* Added some new examples

### 0.1.5 / 2019-07-16 ###

* Make tus uploads to assembly's tus url
* Make assembly wait till completion

### 0.1.4 / 2019-04-27 ###

* Use a fallback version

### 0.1.3 / 2019-04-18 ###

* load sdk version via ResourceBundle

### 0.1.2 / 2019-04-09 ###

* send client version via "Transloadit-Client" header
* Do not use deprecated status_endpoint property
* update tus-java-client version

### 0.1.1 / 2018-04-23 ###

* Allow configurable upload chunk size [#21](https://github.com/transloadit/java-sdk/issues/21)

### 0.1.0 / 2018-04-05 ###

* Support for Pausable/Resumable Asynchronous assemblies
* Add assembly files as Inputstream

### 0.0.6 / 2018-01-19 ###

* Do tus uploads only when there are files to upload.

### 0.0.5 / 2018-01-18 ###

* Check for assembly error before proceeding with tus upload

### 0.0.4 / 2018-01-08 ###

* Remove tus upload chunksize

### 0.0.3 / 2017-05-15 ###

* `Steps.removeStep` method
* Added example project for sample codes
* Maven compliant deployment build. 

### 0.0.2 / 2017-05-12 ###

* `AssemblyResponse.getStepResult` method

### 0.0.1 / 2017-05-09 ###

* Initial release
