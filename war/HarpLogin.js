(($) => {
  /**
   * Convenience function. Save the tokens for later use, for example if the page gets refreshed:
   * Add the token to tokenManager to automatically renew the token when needed
   * Returns the ID token.
   * @param tokens - a single ID or access token or array of such tokens
   * @param tokenManager - token manager used to store tokens
   * @return the ID token
   */
  async function storeTokens(tokens, tokenManager) {
    tokens = Array.isArray(tokens) ? tokens : [tokens];
    tokens.forEach( (token) => {
      const {idToken, accessToken} = token;
      if (idToken) {
        tokenManager.add("idToken", token);
      }
      if (accessToken) {
        tokenManager.add("accessToken", token);
      }
    });
    return await tokenManager.get("accessToken");
  }

  /**
   * A convenience function to call OktaSignIn#renderEl and return its result as a promise
   * @param oktaSignIn - oktaSignIn instance to use
   * @param el - ID of the element in which to render the widget
   * @returns {Promise} - upon success, no handling needed. Redirects to Okta, then back to this page if successful
   */
  async function renderWidget(oktaSignIn, el) {
    return new Promise((resolve, reject) => {
      oktaSignIn.renderEl({el}, (res) => {
        resolve(res);
      }, (err) => {
        reject(err);
      });
    });
  }

  /**
   * Posts the specified token to the server using the form on the page
   * @param token
   */
  function postToken(token) {
    console.dir(token);
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
  async function handleOkta(clientId, baseUrl, harpBaseUrl) {
    const oktaSignIn = new OktaSignIn({
      baseUrl,
      clientId,
      redirectUri: window.location.origin + window.location.pathname,
      authParams: {
        pkce: true,
        display: "page",
        responseType: ["token", "id_token"]
      },
      harpBaseUrl,
      oktaTermsConditionsEndPoint: "mft-signin/terms",
      oktaRedirectEndPoint: "mft-signin/redirect",
      ktaRedirectParamName: "appPageName",
      harpSignUpAppName: "HARP Registration",
      harpRecoveryAppName: "HARP Recovery",
      harpSignUpEndPoint: "register/profile-info",
      harpRecorveryEndPoint: "login/account-recovery",
      oktaHelpEndPoint: "mft-signin/help",
      oktaTermsConditionsContent: "I agree to the ",
      oktaTermsConditionsLinkContent: "Terms and Conditions",
      harpSignUpHeaderContent: "Don't have an account?",
      harpSignUpLinkContent: "Sign Up",
      harpRecoveryContent: "Having trouble logging in?",
      oktaHelpContent: "MFT Help",
      isOktaHelpContentAvailable: false,
      allowRemeberDeviceMFA: false,
      features: {
        "rememberMe": false
      },
    });
    const {authClient} = oktaSignIn;
    const {tokenManager} = authClient;

    // Check if there are tokens in our URL fragment e.g. #token=foo
    // If so, that means Okta has redirected back to this page after a successful login
    if (oktaSignIn.hasTokensInUrl()) {
      try {
        // Get the tokens from the fragment
        const tokens = await authClient.token.parseFromUrl();

        // Store the tokens for later
        const accessToken = await storeTokens(tokens, tokenManager);

        // Send the token to the server
        postToken(accessToken);
      } catch (err) {
        console.error(err);
        throw new Error("Error retrieving tokens from URL fragment");
      }
    } else {
      // It's not an Okta redirect, but we might still have an active session
      const session = await authClient.session.get();

      if (session.status === "ACTIVE") {
        try {
          // Session exists: get tokens
          const tokens = await authClient.token.getWithoutPrompt({
            scopes: ["openid", "email", "profile"]
          });

          // Store the tokens
          const accessToken = await storeTokens(tokens, tokenManager);

          // Send the token to the server
          postToken(accessToken);
        } catch (err) {
          console.error(err);
          throw new Error("Cannot retrieve tokens from active session");
        }
      } else {
        try {
          // No session, show the login form
          await renderWidget(oktaSignIn, "#okta-login-container");
        } catch (err) {
          console.error(err);
          throw new Error("Cannot render Okta widget");
        }
      }
    }
  }

  // Once the DOM is loaded, call our main "handleOkta" function, and handle errors
  $(() => {
    $.ajax({
      "url": "harpLogin",
      "type": "GET"
    }).done(function(props) {
      handleOkta(props.clientId, props.baseUrl, props.harpBaseUrl).then(() => {
        console.log("success"); // FIXME
      }).catch((err) => {
        console.error("Okta Error");
        console.error(err);
      }).finally(() => { // FIXME
        console.log("done!");
      });
    });
  });
})(jQuery);
