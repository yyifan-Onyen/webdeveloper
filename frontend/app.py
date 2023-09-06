from flask import Flask
from flask import request
import os
import requests

BACKEND_HOST = os.getenv('BACKEND_HOST', 'http://localhost:8080')
SERVER_PORT = os.getenv('SERVER_PORT', 8000)
NOSQL_HOST = os.getenv('NOSQL_HOST', 'http://localhost:8080')

# Create Flask app
app = Flask(__name__, static_url_path='/', static_folder='static')

@app.route('/')
def index():
    return app.send_static_file("index.html")

# Test endpoint, ignore
@app.route('/greeting', methods=['GET'])
def greeting():
    response = requests.get(BACKEND_HOST + '/greeting')
    return response.content, response.status_code, response.headers.items()

@app.route('/images', methods=['POST'])
def images():
    response = requests.post(BACKEND_HOST + '/images', data=request.data)
    return response.content, response.status_code, response.headers.items()

@app.route("/image/<userId>", methods=['GET'])
def image(userId):
    response = requests.get(BACKEND_HOST + f'/image/{userId}', data=request.data)
    return response.content, response.status_code, response.headers.items()

@app.route('/validate', methods=['POST'])
def validate():
    response = requests.post(BACKEND_HOST + '/validate', data=request.data)
    return response.content, response.status_code, response.headers.items()
    
@app.route('/form', methods=['POST'])
def form():
    response = requests.post(BACKEND_HOST + '/form', data=request.data)
    return response.content, response.status_code

@app.route('/users', methods=['POST'])
def users():
    response = requests.post(NOSQL_HOST + '/users', data=request.data)
    return response.content, response.status_code

@app.route('/proxy-example', methods=['GET', 'POST'])
def proxy_example():
    if request.method == 'GET':
        # GET 请求的代理逻辑
        response = requests.get(BACKEND_HOST + '/api/data', params=request.args)
    elif request.method == 'POST':
        # POST 请求的代理逻辑
        response = requests.post(BACKEND_HOST + '/api/data', json=request.json)
    return response.text, response.status_code, response.headers.items()

if __name__ == '__main__': 
    app.run(host="0.0.0.0", port=SERVER_PORT, debug=True)
