def queryMap = [:]

def logFile = new File(jdbcFile)

logFile.eachLine { line ->
	 
	 def parts = line.split('\\|')
	 
	 if (parts.size() != 7) //e.g. a commit - no query, does typically not consume time (0ms)
		  return
		  
	 def query = parts[5]
	 def kind = parts[4]

	 def timeMatcher = parts[3] =~ /^(\d+)/
	 
	 
	 
	 def queryTime = Integer.parseInt(timeMatcher[0][1])
	 
	 if (!(kind =~ /statement/))
		  return

	 if (!queryMap[query])
		  queryMap[query] = [count:1, time:queryTime]
	 else
	 {
		  queryMap[query].count++
		  queryMap[query].time += queryTime
	 }

}

//output
def totalQueries = queryMap.collect { it.value.count }.inject(0) {sum, item -> sum + item }
def totalTime = queryMap.collect { it.value.time }.inject(0) {sum, item -> sum + item }

queryMap = queryMap.sort {a, b -> b.value.time <=> a.value.time }
if (totalTime > 0) {
	queryMap = queryMap.findAll { it.value.time/totalTime*100 > 5 }  //filter > 5% of total time
} else {
	queryMap = queryMap.findAll { it.value.time > 5 }
}

result = [
			queryMap:queryMap,
			totalQueries:totalQueries,
			totalTime:totalTime
			]