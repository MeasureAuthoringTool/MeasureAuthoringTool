(function ($) {
  /**
   * Convenience function. Save the tokens for later use, for example if the page gets refreshed:
   * Add the token to tokenManager to automatically renew the token when needed
   * Returns the ID token.
   * @param tokens - a single ID or access token or array of such tokens
   * @param tokenManager - token manager used to store tokens
   * @return the ID token
   */
  function storeTokens(tokens, tokenManager) {
    tokens = Array.isArray(tokens) ? tokens : [tokens];
    tokens.forEach(function (token) {
      const idToken = token.idToken;
      const accessToken = token.accessToken;
      if (idToken) {
        tokenManager.add("idToken", token);
      }
      if (accessToken) {
        tokenManager.add("accessToken", token);
      }
    });
    return tokenManager.get("accessToken");
  }

  /**
   * A convenience function to call OktaSignIn#renderEl and return its result as a promise
   * @param oktaSignIn - oktaSignIn instance to use
   * @param el - ID of the element in which to render the widget
   * @returns {Promise} - upon success, no handling needed. Redirects to Okta, then back to this page if successful
   */
  function renderWidget(oktaSignIn, el) {
    return new Promise(function (resolve, reject) {
      oktaSignIn.renderEl({el: el}, function (res) {
        resolve(res);
      }, function (err) {
        reject(err);
      });
    });
  }

  /**
   * Posts the specified token to the server using the form on the page
   * @param token
   */
  function postToken(token) {
    document.getElementById("loginPost").value = token.accessToken;
    const form = document.getElementById("loginForm");
    form.action = "Mat.html";
    form.submit();
  }

  /**
   * This is the main, top-level logic function, handling all Okta interactions.
   * It"s a separate function so we can easily use async/await
   * @returns {Promise<void>}
   */
  function handleOkta(clientId, baseUrl, harpBaseUrl) {
    return new Promise(function (resolve, reject) {
      const oktaSignIn = new OktaSignIn({
        baseUrl: baseUrl,
        clientId: clientId,
        redirectUri: window.location.origin + window.location.pathname,
        authParams: {
          pkce: true,
          display: "page",
          responseType: ["token", "id_token"]
        },
        harpBaseUrl: harpBaseUrl,
        oktaTermsConditionsEndPoint: "https://www.emeasuretool.cms.gov/terms-and-conditions",
        oktaRedirectEndPoint: "mft-signin/redirect",
        ktaRedirectParamName: "appPageName",
        harpSignUpAppName: "HARP Registration",
        harpRecoveryAppName: "HARP Recovery",
        harpSignUpEndPoint: "register/profile-info",
        harpRecorveryEndPoint: "login/account-recovery",
        oktaHelpEndPoint: "mft-signin/help",
        oktaTermsConditionsContent: "I agree to the Terms and Conditions",
        oktaTermsConditionsLinkContent: "Terms and Conditions",
        oktaTermsConditionsErrorContent: "Please accept the Terms and Conditions",
        harpSignUpHeaderContent: "Don't have an account?",
        harpSignUpLinkContent: "Sign Up",
        harpSignUpUrl: "https://harp.qualitynet.org/register/profile-info",
        harpRecoveryContent: "Having trouble logging in?",
        harpRecoveryUrl: " https://harp.qualitynet.org/login/account-recovery",
        oktaHelpContent: "MFT Help",
        isOktaHelpContentAvailable: false,
        allowRemeberDeviceMFA: false,
        features: {
          "rememberMe": false
        },
      });
      const authClient = oktaSignIn.authClient;
      const tokenManager = authClient.tokenManager;

      // Check if there are tokens in our URL fragment e.g. #token=foo
      // If so, that means Okta has redirected back to this page after a successful login
      if (oktaSignIn.hasTokensInUrl()) {
        // Get the tokens from the fragment
        authClient.token.parseFromUrl().then(function (tokens) {
          // Store the tokens for later
          storeTokens(tokens, tokenManager).then(function (accessToken) {
            // Send the token to the server
            postToken(accessToken);
          }).catch(function (err) {
            console.error("Error storing tokens");
            console.dir(err);
            reject(err);
          });
        }).catch(function (err) {
          console.error("Error parsing tokens from URL fragment");
          console.dir(err);
          reject(err);
        });
      } else {
        // It's not an Okta redirect, but we might still have an active session
        authClient.session.get().then(function (session) {
          if (session.status === "ACTIVE") {
            // Session exists: get tokens
            const options = {
              scopes: ["openid", "email", "profile"]
            };
            authClient.token.getWithoutPrompt(options).then(function (tokens) {
              // Store the tokens
              storeTokens(tokens, tokenManager).then(function (accessToken) {
                // Send the token to the server
                postToken(accessToken);
              }).catch(function (err) {
                console.error("Error storing Okta token");
                console.dir(err);
                reject(err);
              });
            }).catch(function (err) {
              console.error("Error retrieving Okta token");
              console.dir(err);
              reject(err);
            });
          } else {
            // No session, show the login form
            renderWidget(oktaSignIn, "#okta-login-container").catch(function (err) {
              console.error(err);
              console.dir(err);
              throw new Error("Cannot render Okta widget");
            });
          }
        }).catch(function (err) {
          console.error("Error retrieving Okta session");
          console.dir(err);
          reject(err);
        });
      }
    });
  }

  // Once the DOM is loaded, call our main "handleOkta" function, and handle errors
  $(function () {
    $.ajax({
      "url": "harpLogin",
      "type": "GET"
    }).done(function (props) {
      handleOkta(props.clientId, props.baseUrl, props.harpBaseUrl).catch(function (err) {
        console.error("Okta Error");
        console.dir(err);
        console.log("done!");
      });
    });
  });
})(jQuery);
