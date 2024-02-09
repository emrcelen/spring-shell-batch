# 🗃 JBATCH
This project is a console application developed using Spring Shell within the Java Spring Boot ecosystem. This console application allows you to establish a connection with the target database and perform bulk data feeding on the desired table and schema using Spring Batch.
## ⚙️ Technologies
<div >
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" title="Spring Boot"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" title="Maven"/></code>
	<code><img width="50" src="https://github.com/marwin1991/profile-technology-icons/assets/136815194/50342602-8025-4030-b492-550f2eaa4073" alt="RabbitMQ" title="RabbitMQ"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117208740-bfb78400-adf5-11eb-97bb-09072b6bedfc.png" alt="PostgreSQL" title="PostgreSQL"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png" alt="Docker" title="Docker"/></code>
</div>

## 🏷 Usage Example
| Command | Example 
| :-------------: |-------------|
| db model     | db model --url {localhost} --port {port} --name {dbName} --username {dbUserName} --password {dbPassword} --select {db}|
| db source     | db source|
| db table     | db table --name {tableName} --schema {tableSchema}|
| batch model              | batch model --fileName {import} --mail {informationMail} --delimiter {";"}| 
| batch start     | batch start |

### ⚠️ Note
Before starting the application, please ensure that the container is created using Docker Compose.




