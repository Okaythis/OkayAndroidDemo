package com.ogieben.okaydemo.data.repository

import android.text.SpannableStringBuilder
import com.protectoria.psa.dex.common.data.dto.gui_data.OperationType
import com.protectoria.psa.dex.common.data.dto.gui_data.TransactionInfo
import com.protectoria.psa.dex.common.ui.ResourceProvider

class ResourceProviderImpl: ResourceProvider {
  override fun provideTextForBiometricPromtCancelButton(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForEnrollmentDescription(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideScreenshotsNotificationIconId(): Int {
    return 2223334; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForFee(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideScreenshotsChannelName(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForTransactionDetails(p0: TransactionInfo?): SpannableStringBuilder {
    return SpannableStringBuilder(); //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForBiometricPromtSubTitle(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForRecipient(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForEnrollmentTitle(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForConfirmButton(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForBiometricPromtDescription(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideScreenshotsNotificationText(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForAuthScreenTitle(p0: OperationType?): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForBiometricPromtTitle(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForConfirmBiometricButton(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForPaymentDetailsButton(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }

  override fun provideTextForAuthorizationProgressView(): String {
    return "Place holder"; //To change body of created functions use File | Settings | File Templates.
  }
}