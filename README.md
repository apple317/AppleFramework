AppleFramework
==============

本项目基于快速开发android项目目的，总结多年项目开发经验，本项目基于mvc架构，其中把数据层、网络交互层、页面逻辑处理层完全分离，降低多层之间藕合度。

通过封装网络客户端代理方式,完全分离网络层，项目中可以随意使用不同网络库，解析回来使用工厂模式实现各个具体解析实体模型。

再本项目总结多年开发，把所有基类activity、application、dialog、Fragment 公共抽离，可以直接使用里面发申请方法、事件集中处理、网络事件回传
方法中，把业务逻辑写代码分离如下：
  /**
	 * 初始化布局和控件
	 * 
	 * @param bundle
	 */
	protected abstract View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);

	/**
	 * 初始化监听
	 */
	protected abstract void initLisener();
	
	/**
	 * 初始化数据
	 */
	protected abstract void initData(Bundle savedInstanceState);

 
 本项目中集合微信、微博分享功能，只要简单配置就可以直接使用，支持微博授权，微信、微博各种分享模式等。



