Play 2 Activiti Plugin
======================

This is a plugin for Play 2, providing the following:

* Starting and stopping an Activiti Process engine with Play
* Automatically deploying BPMN process definitions from the `conf/processes` directory
* Traits that make working with Activiti nicer from Scala
* A utility to make Activiti transactions join [Squeryl](http://www.squeryl.org) transactions, if they are inside a Squeryl `transaction` or `inTransaction` block.
* Utilities to automatically retry exceptions of type `TransientException` when they occur in jobs.

Versions
========

* There is no version for Play 2.0
* Version 0.1-SNAPSHOT is the latest version for Play 2.1

Quick start
===========

Add the following dependency to your application's `Build.scala`:

    "com.lunatech" %% "play2-activiti" % "0.1-SNAPSHOT"

This requires one of the following resolvers:

    // For release versions
    "Lunatech public releases" at "http://artifactory.lunatech.com/artifactory/releases-public"
    
    // For SNAPSHOT versions
    "Lunatech public snapshots" at "http://artifactory.lunatech.com/artifactory/snapshots-public"

Add a configuration setting for the processes you want to load:

    processDefinitions = [
      "Foo.bpmn",
      "Bar.bpmn"
    ]

API Docs
========

The latest api documentation can be found at http://lunatech.com/play2-activiti/latest/api

Examples
========

TODO

License
=======

Copyright 2013 Lunatech (http://www.lunatech.com).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
