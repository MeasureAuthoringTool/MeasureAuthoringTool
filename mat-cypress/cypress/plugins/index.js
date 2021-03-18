/// <reference types="cypress" />
const fs = require('fs-extra');
const path = require('path');

function getConfigurationByFile(file) {
  const pathToConfigFile = path.resolve('config', `${file}.json`);

  return fs.readJson(pathToConfigFile);
}

// plugins file
module.exports = (on, config) => {
  const file = config.env.configFile || 'local';

  return getConfigurationByFile(file);
};
