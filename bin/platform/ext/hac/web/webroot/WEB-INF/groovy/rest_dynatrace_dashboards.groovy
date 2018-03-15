
//username password host given
//result is response

def url = "http://${host}/rest/management/dashboards".toURL()

result = [:]
result.host = host
result.dashboards = [:]

def con = url.openConnection()
String userpassword = username + ":" + password
con.setRequestProperty("Authorization", "Basic "+ userpassword.bytes.encodeBase64().toString())
 
def dashboards = new XmlSlurper().parse(con.inputStream)

dashboards.dashboard.each { dashboard ->
	println "${dashboard.@id} -> ${dashboard.@href}"
	result.dashboards[dashboard.@id.toString()] = [href:dashboard.@href.toString(), jnlp:dashboard.@jnlp.toString() ,desc:dashboard.@description.toString() ]
}

 
if (con != null)
	con.disconnect()
	
