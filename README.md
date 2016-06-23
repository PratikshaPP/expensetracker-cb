# expensetracker-cb
spring-boot vaadin app showing CRUD operations and a few filter and search by date operation using Couchbase as datastore 

SourceCode - You can find the source code at https://github.com/ratchakr/expensetracker-cb

Email: You can contact me at my email:ratnopam_ch@yahoo.co.in or tweet to me @ratnopam

2 minute screencast - View the overview at https://www.dropbox.com/s/eb5xinq05tnw9tr/expensetracker.mp4?dl=0

Please follow the below steps to run the app. The steps given below are used for running the app in Docker

1. Run a customized couchbase image couchbase 4.5 version:

docker run -d -p 8091-8093:8091-8093 -p 11210:11210 chakrar27/couchbase-server-4.5

2. Access the couchbase UI at this url -> http://192.168.99.100:8091/ui/index.html
   Login with Administrator/password 
   
3. Create a new bucket named "default".

4. Set Up cluster with default values

5. Create Primary Index on `default` bucket:
CREATE PRIMARY INDEX ON `default`;

6. Create view to back expense tracker app
design/dev_expenseDetail
all
function (doc, meta) {
    if (doc._class == "com.expensetrakcer.cb.ExpenseDetail") {
        emit(meta.id, null);
    }  
}
Publish the view to production

7. Run expensetracker app
docker run -d -p 8080:8080 chakrar27/expensetracker-cb

8. Access the expense tracker app at 192.168.99.100:8080/