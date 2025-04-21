from flask import Flask, request, jsonify
from dockerfile_parse import DockerfileParser

app = Flask(__name__)

# TODO: somente para teste, remover após info no BD
collect_response = [
  {"id": "xpto", "urlDockerfile": "path.to/dockerfile"}
]

@app.route('/collect', methods=['GET'])
def post_collect():
  return jsonify({"data": collect_response})

if __name__ == '__main__':
  app.run(debug=True)