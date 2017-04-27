require 'rest-client'
require 'base64'
require 'json'

username = 'admin'
password = 'Service!23'
base_url = 'https://dev26709.service-now.com'
object_url = '/api/now/table/incident'

p "Create a service now ticket"

# This must be valid json string with valid fields and values from table
obj_hash = {"short_description" => "Test with java post"}

auth = 'Basic ' + Base64.encode64("#{username}:#{password}").chomp

result = JSON.parse(RestClient.post(base_url + object_url, obj_hash.to_json, authorization: auth, accept: :json, content_type: :json))

p result

p "SUCCESS"