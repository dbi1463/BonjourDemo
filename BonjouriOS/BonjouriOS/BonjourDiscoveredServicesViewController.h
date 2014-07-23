//
//  BonjourDiscoveredServicesViewController.h
//  BonjouriOS
//
//  Created by Spirit on 7/23/14.
//  Copyright (c) 2014 funymph. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "BonjourDiscoveredServicesDelegate.h"

@interface BonjourDiscoveredServicesViewController : UIViewController<NSNetServiceDelegate, BonjourDiscoveredServicesDelegate>

@property (nonatomic) BOOL discovering;

@property (weak, nonatomic) IBOutlet UIButton* publishButton;
@property (weak, nonatomic) IBOutlet UIButton* discoverButton;
@property (weak, nonatomic) IBOutlet UITableView* servicesView;

- (IBAction)pushlishService:(id)sender;
- (IBAction)discoverServices:(id)sender;

@end
